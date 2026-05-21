package cm.agrocam.digitrans.scm.controller;

import cm.agrocam.digitrans.scm.dto.ApiResponse;
import cm.agrocam.digitrans.scm.dto.TrackingEventRequest;
import cm.agrocam.digitrans.scm.dto.TrackingEventResponse;
import cm.agrocam.digitrans.scm.service.TrackingEventService;
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
@RequestMapping("/api/tracking-events")
@Tag(name = "Shipment Tracking Events Interface", description = "Endpoints for registering and viewing tracking checkpoint events for shipments")
@PreAuthorize("hasAnyRole('AGENT', 'MANAGER')")
public class TrackingEventController {

    private static final Logger log = LoggerFactory.getLogger(TrackingEventController.class);
    private final TrackingEventService trackingEventService;

    public TrackingEventController(TrackingEventService trackingEventService) {
        this.trackingEventService = trackingEventService;
    }

    @GetMapping
    @Operation(summary = "Get all tracking events", description = "Retrieve list of all registered tracking checkpoint events")
    public ResponseEntity<ApiResponse<List<TrackingEventResponse>>> getAllEvents() {
        log.info("REST request to fetch all tracking events");
        List<TrackingEventResponse> events = trackingEventService.getAllEvents();
        return ResponseEntity.ok(ApiResponse.success(events, "Tracking events retrieved successfully"));
    }

    @GetMapping("/shipment/{shipmentId}")
    @Operation(summary = "Get tracking events by shipment ID", description = "Retrieve all tracking checkpoint events recorded for a specific shipment")
    public ResponseEntity<ApiResponse<List<TrackingEventResponse>>> getEventsByShipmentId(@PathVariable Long shipmentId) {
        log.info("REST request to fetch tracking events for shipment ID: {}", shipmentId);
        List<TrackingEventResponse> events = trackingEventService.getEventsByShipmentId(shipmentId);
        return ResponseEntity.ok(ApiResponse.success(events, "Tracking events for shipment retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tracking event by ID", description = "Retrieve a specific tracking checkpoint event details")
    public ResponseEntity<ApiResponse<TrackingEventResponse>> getEventById(@PathVariable Long id) {
        log.info("REST request to fetch tracking event ID: {}", id);
        TrackingEventResponse event = trackingEventService.getEventById(id);
        return ResponseEntity.ok(ApiResponse.success(event, "Tracking event retrieved successfully"));
    }

    @PostMapping
    @Operation(summary = "Create tracking event", description = "Register a new transit checkpoint checkpoint event for a shipment")
    public ResponseEntity<ApiResponse<TrackingEventResponse>> createEvent(@Valid @RequestBody TrackingEventRequest request) {
        log.info("REST request to create tracking event for shipment ID: {}", request.getShipmentId());
        TrackingEventResponse event = trackingEventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(event, "Tracking event created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tracking event", description = "Modify an existing tracking checkpoint event details")
    public ResponseEntity<ApiResponse<TrackingEventResponse>> updateEvent(@PathVariable Long id, @Valid @RequestBody TrackingEventRequest request) {
        log.info("REST request to update tracking event ID: {}", id);
        TrackingEventResponse event = trackingEventService.updateEvent(id, request);
        return ResponseEntity.ok(ApiResponse.success(event, "Tracking event updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tracking event", description = "Remove a tracking event from the records")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        log.info("REST request to delete tracking event ID: {}", id);
        trackingEventService.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tracking event deleted successfully"));
    }
}
