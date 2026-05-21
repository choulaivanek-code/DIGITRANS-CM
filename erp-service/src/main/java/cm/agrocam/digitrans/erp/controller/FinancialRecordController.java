package cm.agrocam.digitrans.erp.controller;

import cm.agrocam.digitrans.erp.dto.ApiResponse;
import cm.agrocam.digitrans.erp.dto.FinancialRequest;
import cm.agrocam.digitrans.erp.dto.FinancialResponse;
import cm.agrocam.digitrans.erp.service.FinancialRecordService;
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
@RequestMapping("/api/financials")
@Tag(name = "Financial Accounting Interface", description = "Endpoints for managing revenues and expenses of AGROCAM S.A.")
public class FinancialRecordController {

    private static final Logger log = LoggerFactory.getLogger(FinancialRecordController.class);
    private final FinancialRecordService financialRecordService;

    public FinancialRecordController(FinancialRecordService financialRecordService) {
        this.financialRecordService = financialRecordService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Get all financial records", description = "Retrieve list of all revenue and expense transactions")
    public ResponseEntity<ApiResponse<List<FinancialResponse>>> getAllFinancialRecords() {
        log.info("REST request to fetch all financial records");
        List<FinancialResponse> records = financialRecordService.getAllFinancialRecords();
        return ResponseEntity.ok(ApiResponse.success(records, "Financial records retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Get financial record by ID", description = "Retrieve transactional details for a specific record ID")
    public ResponseEntity<ApiResponse<FinancialResponse>> getFinancialRecordById(@PathVariable Long id) {
        log.info("REST request to fetch financial record by ID: {}", id);
        FinancialResponse record = financialRecordService.getFinancialRecordById(id);
        return ResponseEntity.ok(ApiResponse.success(record, "Financial record retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    @Operation(summary = "Create financial record", description = "Log a new revenue or expense transaction")
    public ResponseEntity<ApiResponse<FinancialResponse>> createFinancialRecord(@Valid @RequestBody FinancialRequest request) {
        log.info("REST request to create financial record: ref={}", request.getReference());
        FinancialResponse record = financialRecordService.createFinancialRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(record, "Financial record created successfully"));
    }
}
