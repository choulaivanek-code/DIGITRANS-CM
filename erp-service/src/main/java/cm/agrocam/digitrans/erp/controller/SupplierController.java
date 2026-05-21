package cm.agrocam.digitrans.erp.controller;

import cm.agrocam.digitrans.erp.dto.ApiResponse;
import cm.agrocam.digitrans.erp.dto.SupplierRequest;
import cm.agrocam.digitrans.erp.dto.SupplierResponse;
import cm.agrocam.digitrans.erp.service.SupplierService;
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
@RequestMapping("/api/suppliers")
@Tag(name = "Supplier Management Interface", description = "Endpoints for managing AGROCAM S.A. external suppliers")
public class SupplierController {

    private static final Logger log = LoggerFactory.getLogger(SupplierController.class);
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE', 'RH')")
    @Operation(summary = "Get all suppliers", description = "Retrieve list of all active enterprise suppliers")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllSuppliers() {
        log.info("REST request to fetch all suppliers");
        List<SupplierResponse> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE', 'RH')")
    @Operation(summary = "Get supplier by ID", description = "Retrieve supplier details for a specific ID")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(@PathVariable Long id) {
        log.info("REST request to fetch supplier by ID: {}", id);
        SupplierResponse supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE', 'RH')")
    @Operation(summary = "Create supplier", description = "Register a new external supplier into the system")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(@Valid @RequestBody SupplierRequest request) {
        log.info("REST request to create supplier: company={}", request.getCompanyName());
        SupplierResponse supplier = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(supplier, "Supplier registered successfully"));
    }
}
