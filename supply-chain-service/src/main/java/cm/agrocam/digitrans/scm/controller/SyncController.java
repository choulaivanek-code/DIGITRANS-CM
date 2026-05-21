package cm.agrocam.digitrans.scm.controller;

import cm.agrocam.digitrans.scm.dto.ApiResponse;
import cm.agrocam.digitrans.scm.dto.SyncPacket;
import cm.agrocam.digitrans.scm.dto.SyncResponse;
import cm.agrocam.digitrans.scm.service.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
@Tag(name = "Offline Data Synchronization Interface", description = "Endpoints for synchronizing offline collected shipments and tracking events")
@PreAuthorize("hasAnyRole('AGENT', 'MANAGER')")
public class SyncController {

    private static final Logger log = LoggerFactory.getLogger(SyncController.class);
    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping
    @Operation(summary = "Synchronize offline data", description = "Submit offline shipments and tracking event packets. Implements idempotency controls to upsert existing records by identifier.")
    public ResponseEntity<ApiResponse<SyncResponse>> syncOfflineData(@Valid @RequestBody SyncPacket packet) {
        log.info("REST request to sync offline data packet");
        SyncResponse response = syncService.syncOfflineData(packet);
        return ResponseEntity.ok(ApiResponse.success(response, "Offline synchronization completed successfully"));
    }
}
