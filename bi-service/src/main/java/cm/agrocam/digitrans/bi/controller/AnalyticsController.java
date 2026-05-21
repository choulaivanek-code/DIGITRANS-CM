package cm.agrocam.digitrans.bi.controller;

import cm.agrocam.digitrans.bi.dto.ApiResponse;
import cm.agrocam.digitrans.bi.dto.EmployeeReport;
import cm.agrocam.digitrans.bi.dto.SalesReport;
import cm.agrocam.digitrans.bi.dto.SupplyReport;
import cm.agrocam.digitrans.bi.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Business Intelligence Aggregators", description = "High-level consolidated reports aggregating multiple company departments")
@PreAuthorize("hasAuthority('ROLE_DIRECTOR')")
public class AnalyticsController {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsController.class);
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/sales")
    @Operation(summary = "Get Consolidated Sales Report", description = "Aggregates restaurant orders, revenues, and top performance locations")
    public ResponseEntity<ApiResponse<SalesReport>> getSalesReport() {
        log.info("REST request to fetch consolidated sales report");
        SalesReport report = analyticsService.getSalesReport();
        return ResponseEntity.ok(ApiResponse.success(report, "Sales report generated successfully"));
    }

    @GetMapping("/supply")
    @Operation(summary = "Get Consolidated Logistics and Supply Report", description = "Aggregates stock totals, active delivery shipments, and success rates")
    public ResponseEntity<ApiResponse<SupplyReport>> getSupplyReport() {
        log.info("REST request to fetch consolidated supply chain report");
        SupplyReport report = analyticsService.getSupplyReport();
        return ResponseEntity.ok(ApiResponse.success(report, "Supply chain report generated successfully"));
    }

    @GetMapping("/employees")
    @Operation(summary = "Get Consolidated Staff and Financial Efficiency Report", description = "Aggregates personnel counts, total payrolls, and financial ratios")
    public ResponseEntity<ApiResponse<EmployeeReport>> getEmployeeReport() {
        log.info("REST request to fetch consolidated staff/financial report");
        EmployeeReport report = analyticsService.getEmployeeReport();
        return ResponseEntity.ok(ApiResponse.success(report, "Staff/Financial report generated successfully"));
    }
}
