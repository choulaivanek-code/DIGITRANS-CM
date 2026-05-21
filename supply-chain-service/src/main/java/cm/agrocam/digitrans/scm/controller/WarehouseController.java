package cm.agrocam.digitrans.scm.controller;

import cm.agrocam.digitrans.scm.dto.ApiResponse;
import cm.agrocam.digitrans.scm.dto.WarehouseRequest;
import cm.agrocam.digitrans.scm.dto.WarehouseResponse;
import cm.agrocam.digitrans.scm.service.WarehouseService;
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
@RequestMapping("/api/warehouses")
@Tag(name = "Warehouse Inventory Interface", description = "Endpoints for managing and viewing warehouse capacities and storage locations")
@PreAuthorize("hasAnyRole('AGENT', 'MANAGER')")
public class WarehouseController {

    private static final Logger log = LoggerFactory.getLogger(WarehouseController.class);
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    @Operation(summary = "Get all warehouses", description = "Retrieve list of all registered warehouses")
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> getAllWarehouses() {
        log.info("REST request to fetch all warehouses");
        List<WarehouseResponse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(ApiResponse.success(warehouses, "Warehouses retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID", description = "Retrieve detail information for a specific warehouse ID")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseById(@PathVariable Long id) {
        log.info("REST request to fetch warehouse by ID: {}", id);
        WarehouseResponse warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Warehouse retrieved successfully"));
    }

    @PostMapping
    @Operation(summary = "Create warehouse", description = "Register a new warehouse hub location")
    public ResponseEntity<ApiResponse<WarehouseResponse>> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        log.info("REST request to create warehouse: {}", request.getName());
        WarehouseResponse warehouse = warehouseService.createWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(warehouse, "Warehouse created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update warehouse", description = "Modify an existing warehouse's details and capacity")
    public ResponseEntity<ApiResponse<WarehouseResponse>> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseRequest request) {
        log.info("REST request to update warehouse ID: {}", id);
        WarehouseResponse warehouse = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(ApiResponse.success(warehouse, "Warehouse updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete warehouse", description = "Remove a warehouse from the active registry")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
        log.info("REST request to delete warehouse ID: {}", id);
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Warehouse deleted successfully"));
    }
}
