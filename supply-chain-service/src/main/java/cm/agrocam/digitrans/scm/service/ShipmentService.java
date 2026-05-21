package cm.agrocam.digitrans.scm.service;

import cm.agrocam.digitrans.scm.dto.ShipmentRequest;
import cm.agrocam.digitrans.scm.dto.ShipmentResponse;
import cm.agrocam.digitrans.scm.dto.WarehouseResponse;
import cm.agrocam.digitrans.scm.entity.Shipment;
import cm.agrocam.digitrans.scm.entity.Warehouse;
import cm.agrocam.digitrans.scm.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.scm.repository.ShipmentRepository;
import cm.agrocam.digitrans.scm.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseService warehouseService;

    @Transactional(readOnly = true)
    public List<ShipmentResponse> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        return mapToResponse(shipment);
    }

    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentByTrackingNumber(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with tracking number: " + trackingNumber));
        return mapToResponse(shipment);
    }

    @Transactional
    public ShipmentResponse createShipment(ShipmentRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getOriginWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + request.getOriginWarehouseId()));

        if (shipmentRepository.findByTrackingNumber(request.getTrackingNumber()).isPresent()) {
            throw new IllegalArgumentException("Shipment already exists with tracking number: " + request.getTrackingNumber());
        }

        Shipment shipment = Shipment.builder()
                .trackingNumber(request.getTrackingNumber())
                .originWarehouse(warehouse)
                .destinationAddress(request.getDestinationAddress())
                .status(request.getStatus())
                .departureDate(request.getDepartureDate())
                .arrivalDate(request.getArrivalDate())
                .build();

        Shipment saved = shipmentRepository.save(shipment);
        return mapToResponse(saved);
    }

    @Transactional
    public ShipmentResponse updateShipment(Long id, ShipmentRequest request) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));

        Warehouse warehouse = warehouseRepository.findById(request.getOriginWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + request.getOriginWarehouseId()));

        if (!shipment.getTrackingNumber().equals(request.getTrackingNumber()) &&
                shipmentRepository.findByTrackingNumber(request.getTrackingNumber()).isPresent()) {
            throw new IllegalArgumentException("Another shipment already exists with tracking number: " + request.getTrackingNumber());
        }

        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setOriginWarehouse(warehouse);
        shipment.setDestinationAddress(request.getDestinationAddress());
        shipment.setStatus(request.getStatus());
        shipment.setDepartureDate(request.getDepartureDate());
        shipment.setArrivalDate(request.getArrivalDate());

        Shipment updated = shipmentRepository.save(shipment);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        shipmentRepository.delete(shipment);
    }

    public ShipmentResponse mapToResponse(Shipment shipment) {
        WarehouseResponse whResponse = null;
        if (shipment.getOriginWarehouse() != null) {
            whResponse = warehouseService.mapToResponse(shipment.getOriginWarehouse());
        }
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .originWarehouse(whResponse)
                .destinationAddress(shipment.getDestinationAddress())
                .status(shipment.getStatus())
                .departureDate(shipment.getDepartureDate())
                .arrivalDate(shipment.getArrivalDate())
                .build();
    }
}
