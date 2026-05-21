package cm.agrocam.digitrans.scm.controller;

import cm.agrocam.digitrans.scm.dto.ApiResponse;
import cm.agrocam.digitrans.scm.dto.ShipmentRequest;
import cm.agrocam.digitrans.scm.dto.ShipmentResponse;
import cm.agrocam.digitrans.scm.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipment Logistics Interface", description = "Endpoints for managing, creating, and updating logistics shipments")
@PreAuthorize("hasAnyRole('AGENT', 'MANAGER')")
public class ShipmentController {

    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    @Operation(summary = "Get all shipments", description = "Retrieve list of all registered active shipments")
    public ResponseEntity<ApiResponse<List<ShipmentResponse>>> getAllShipments() {
        log.info("REST request to fetch all shipments");
        List<ShipmentResponse> shipments = shipmentService.getAllShipments();
        return ResponseEntity.ok(ApiResponse.success(shipments, "Shipments retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shipment by ID", description = "Retrieve details for a specific shipment ID")
    public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentById(@PathVariable Long id) {
        log.info("REST request to fetch shipment by ID: {}", id);
        ShipmentResponse shipment = shipmentService.getShipmentById(id);
        return ResponseEntity.ok(ApiResponse.success(shipment, "Shipment retrieved successfully"));
    }

    @GetMapping("/tracking/{trackingNumber}")
    @Operation(summary = "Get shipment by tracking number", description = "Retrieve details for a specific shipment tracking number")
    public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        log.info("REST request to fetch shipment by tracking number: {}", trackingNumber);
        ShipmentResponse shipment = shipmentService.getShipmentByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(ApiResponse.success(shipment, "Shipment retrieved successfully"));
    }

    @PostMapping
    @Operation(summary = "Create shipment", description = "Register a new cargo shipment")
    public ResponseEntity<ApiResponse<ShipmentResponse>> createShipment(@Valid @RequestBody ShipmentRequest request) {
        log.info("REST request to create shipment tracking number: {}", request.getTrackingNumber());
        ShipmentResponse shipment = shipmentService.createShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(shipment, "Shipment created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shipment", description = "Modify details for an existing active shipment")
    public ResponseEntity<ApiResponse<ShipmentResponse>> updateShipment(@PathVariable Long id, @Valid @RequestBody ShipmentRequest request) {
        log.info("REST request to update shipment ID: {}", id);
        ShipmentResponse shipment = shipmentService.updateShipment(id, request);
        return ResponseEntity.ok(ApiResponse.success(shipment, "Shipment updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shipment", description = "Remove a shipment from the logistics registry")
    public ResponseEntity<ApiResponse<Void>> deleteShipment(@PathVariable Long id) {
        log.info("REST request to delete shipment ID: {}", id);
        shipmentService.deleteShipment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Shipment deleted successfully"));
    }
}
