package cm.agrocam.digitrans.scm.service;

import cm.agrocam.digitrans.scm.dto.*;
import cm.agrocam.digitrans.scm.entity.Shipment;
import cm.agrocam.digitrans.scm.entity.TrackingEvent;
import cm.agrocam.digitrans.scm.entity.Warehouse;
import cm.agrocam.digitrans.scm.repository.ShipmentRepository;
import cm.agrocam.digitrans.scm.repository.TrackingEventRepository;
import cm.agrocam.digitrans.scm.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SyncService {

    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    private final ShipmentRepository shipmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final TrackingEventRepository trackingEventRepository;

    @Transactional
    public SyncResponse syncOfflineData(SyncPacket packet) {
        int processed = 0;
        int ignored = 0;
        int errors = 0;
        List<String> details = new ArrayList<>();

        // Maps client-side shipment ID from the packet to the actual database ID after upsert
        Map<Long, Long> clientToDbShipmentIdMap = new HashMap<>();

        log.info("Starting offline sync processing: {} shipments, {} tracking events",
                packet.getShipments() != null ? packet.getShipments().size() : 0,
                packet.getTrackingEvents() != null ? packet.getTrackingEvents().size() : 0);

        // 1. Process Shipments
        if (packet.getShipments() != null) {
            for (ShipmentRequest req : packet.getShipments()) {
                try {
                    Optional<Shipment> existingShipment = Optional.empty();

                    // Try finding by ID first
                    if (req.getId() != null) {
                        existingShipment = shipmentRepository.findById(req.getId());
                    }

                    // Try finding by unique tracking number if not found by ID
                    if (existingShipment.isEmpty() && req.getTrackingNumber() != null) {
                        existingShipment = shipmentRepository.findByTrackingNumber(req.getTrackingNumber());
                    }

                    // Retrieve parent warehouse
                    Optional<Warehouse> whOpt = warehouseRepository.findById(req.getOriginWarehouseId());
                    if (whOpt.isEmpty()) {
                        errors++;
                        details.add("Shipment [Tracking: " + req.getTrackingNumber() + "]: Warehouse ID " + req.getOriginWarehouseId() + " not found.");
                        continue;
                    }

                    Warehouse warehouse = whOpt.get();
                    Shipment shipment;

                    if (existingShipment.isPresent()) {
                        // Update existing shipment (Idempotence)
                        shipment = existingShipment.get();
                        shipment.setTrackingNumber(req.getTrackingNumber());
                        shipment.setOriginWarehouse(warehouse);
                        shipment.setDestinationAddress(req.getDestinationAddress());
                        shipment.setStatus(req.getStatus());
                        shipment.setDepartureDate(req.getDepartureDate());
                        shipment.setArrivalDate(req.getArrivalDate());
                        
                        Shipment saved = shipmentRepository.save(shipment);
                        processed++;
                        details.add("Shipment [ID: " + saved.getId() + ", Tracking: " + req.getTrackingNumber() + "]: Updated (Upsert).");
                        
                        if (req.getId() != null) {
                            clientToDbShipmentIdMap.put(req.getId(), saved.getId());
                        }
                    } else {
                        // Insert new shipment
                        shipment = Shipment.builder()
                                .trackingNumber(req.getTrackingNumber())
                                .originWarehouse(warehouse)
                                .destinationAddress(req.getDestinationAddress())
                                .status(req.getStatus())
                                .departureDate(req.getDepartureDate())
                                .arrivalDate(req.getArrivalDate())
                                .build();

                        Shipment saved = shipmentRepository.save(shipment);
                        processed++;
                        details.add("Shipment [ID: " + saved.getId() + ", Tracking: " + req.getTrackingNumber() + "]: Created.");
                        
                        if (req.getId() != null) {
                            clientToDbShipmentIdMap.put(req.getId(), saved.getId());
                        }
                    }

                } catch (Exception e) {
                    errors++;
                    details.add("Shipment [Tracking: " + req.getTrackingNumber() + "]: Error: " + e.getMessage());
                    log.error("Error syncing shipment", e);
                }
            }
        }

        // 2. Process Tracking Events
        if (packet.getTrackingEvents() != null) {
            for (TrackingEventRequest req : packet.getTrackingEvents()) {
                try {
                    // Resolve shipment ID from mapping map or direct DB lookup
                    Long resolvedShipmentId = req.getShipmentId();
                    if (clientToDbShipmentIdMap.containsKey(req.getShipmentId())) {
                        resolvedShipmentId = clientToDbShipmentIdMap.get(req.getShipmentId());
                    }

                    Optional<Shipment> shOpt = shipmentRepository.findById(resolvedShipmentId);
                    if (shOpt.isEmpty()) {
                        errors++;
                        details.add("TrackingEvent [Location: " + req.getEventLocation() + "]: Shipment ID " + req.getShipmentId() + " not found.");
                        continue;
                    }

                    Shipment shipment = shOpt.get();
                    Optional<TrackingEvent> existingEvent = Optional.empty();

                    // Check by ID if provided
                    if (req.getId() != null) {
                        existingEvent = trackingEventRepository.findById(req.getId());
                    }

                    // Check for duplicate tracking event (same shipment, location, time) to prevent duplicate sync packets
                    if (existingEvent.isEmpty()) {
                        existingEvent = trackingEventRepository.findByShipmentId(shipment.getId()).stream()
                                .filter(e -> e.getEventLocation().equalsIgnoreCase(req.getEventLocation()) 
                                        && e.getEventTime().isEqual(req.getEventTime()))
                                    .findFirst();
                    }

                    if (existingEvent.isPresent()) {
                        // Update tracking event if it has differences, or just ignore if it is identical
                        TrackingEvent event = existingEvent.get();
                        boolean hasChanges = !event.getStatusDescription().equals(req.getStatusDescription());
                        
                        if (hasChanges) {
                            event.setStatusDescription(req.getStatusDescription());
                            trackingEventRepository.save(event);
                            processed++;
                            details.add("TrackingEvent [ID: " + event.getId() + "]: Updated description.");
                        } else {
                            ignored++;
                            details.add("TrackingEvent [ID: " + event.getId() + "]: Ignored (Already exists with identical data).");
                        }
                    } else {
                        // Create new event
                        TrackingEvent event = TrackingEvent.builder()
                                .shipment(shipment)
                                .eventLocation(req.getEventLocation())
                                .statusDescription(req.getStatusDescription())
                                .eventTime(req.getEventTime())
                                .build();

                        TrackingEvent saved = trackingEventRepository.save(event);
                        processed++;
                        details.add("TrackingEvent [ID: " + saved.getId() + "]: Created.");
                    }

                } catch (Exception e) {
                    errors++;
                    details.add("TrackingEvent [Location: " + req.getEventLocation() + "]: Error: " + e.getMessage());
                    log.error("Error syncing tracking event", e);
                }
            }
        }

        log.info("Offline sync finished: {} processed, {} ignored, {} errors", processed, ignored, errors);

        return SyncResponse.builder()
                .processedCount(processed)
                .ignoredCount(ignored)
                .errorCount(errors)
                .details(details)
                .build();
    }
}
