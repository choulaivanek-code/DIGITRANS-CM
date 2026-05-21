package cm.agrocam.digitrans.scm.service;

import cm.agrocam.digitrans.scm.dto.TrackingEventRequest;
import cm.agrocam.digitrans.scm.dto.TrackingEventResponse;
import cm.agrocam.digitrans.scm.entity.Shipment;
import cm.agrocam.digitrans.scm.entity.TrackingEvent;
import cm.agrocam.digitrans.scm.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.scm.repository.ShipmentRepository;
import cm.agrocam.digitrans.scm.repository.TrackingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackingEventService {

    private final TrackingEventRepository trackingEventRepository;
    private final ShipmentRepository shipmentRepository;

    @Transactional(readOnly = true)
    public List<TrackingEventResponse> getAllEvents() {
        return trackingEventRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrackingEventResponse> getEventsByShipmentId(Long shipmentId) {
        return trackingEventRepository.findByShipmentId(shipmentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TrackingEventResponse getEventById(Long id) {
        TrackingEvent event = trackingEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingEvent not found with id: " + id));
        return mapToResponse(event);
    }

    @Transactional
    public TrackingEventResponse createEvent(TrackingEventRequest request) {
        Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + request.getShipmentId()));

        TrackingEvent event = TrackingEvent.builder()
                .shipment(shipment)
                .eventLocation(request.getEventLocation())
                .statusDescription(request.getStatusDescription())
                .eventTime(request.getEventTime())
                .build();

        TrackingEvent saved = trackingEventRepository.save(event);
        return mapToResponse(saved);
    }

    @Transactional
    public TrackingEventResponse updateEvent(Long id, TrackingEventRequest request) {
        TrackingEvent event = trackingEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingEvent not found with id: " + id));

        Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + request.getShipmentId()));

        event.setShipment(shipment);
        event.setEventLocation(request.getEventLocation());
        event.setStatusDescription(request.getStatusDescription());
        event.setEventTime(request.getEventTime());

        TrackingEvent updated = trackingEventRepository.save(event);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteEvent(Long id) {
        TrackingEvent event = trackingEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingEvent not found with id: " + id));
        trackingEventRepository.delete(event);
    }

    public TrackingEventResponse mapToResponse(TrackingEvent event) {
        return TrackingEventResponse.builder()
                .id(event.getId())
                .shipmentId(event.getShipment().getId())
                .eventLocation(event.getEventLocation())
                .statusDescription(event.getStatusDescription())
                .eventTime(event.getEventTime())
                .build();
    }
}
