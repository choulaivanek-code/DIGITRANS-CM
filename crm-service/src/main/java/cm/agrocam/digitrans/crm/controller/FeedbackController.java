package cm.agrocam.digitrans.crm.controller;

import cm.agrocam.digitrans.crm.dto.ApiResponse;
import cm.agrocam.digitrans.crm.dto.FeedbackRequest;
import cm.agrocam.digitrans.crm.dto.FeedbackResponse;
import cm.agrocam.digitrans.crm.service.FeedbackService;
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
@RequestMapping("/api/feedbacks")
@Tag(name = "Customer Feedback Interface", description = "Endpoints for managing client ratings and reviews")
@PreAuthorize("hasAnyRole('MANAGER', 'STAFF')")
public class FeedbackController {

    private static final Logger log = LoggerFactory.getLogger(FeedbackController.class);
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    @Operation(summary = "Get all feedbacks", description = "Retrieve list of all customer dining feedback logs")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getAllFeedbacks() {
        log.info("REST request to fetch all feedbacks");
        List<FeedbackResponse> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(ApiResponse.success(feedbacks, "Feedbacks retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID", description = "Retrieve a specific feedback review by ID")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedbackById(@PathVariable Long id) {
        log.info("REST request to fetch feedback by ID: {}", id);
        FeedbackResponse feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(ApiResponse.success(feedback, "Feedback retrieved successfully"));
    }

    @PostMapping
    @Operation(summary = "Submit feedback", description = "Log a new customer rating and review for a purchase")
    public ResponseEntity<ApiResponse<FeedbackResponse>> createFeedback(@Valid @RequestBody FeedbackRequest request) {
        log.info("REST request to submit feedback: rating={}", request.getRating());
        FeedbackResponse feedback = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(feedback, "Feedback logged successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feedback", description = "Remove feedback review from the system")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        log.info("REST request to delete feedback with ID: {}", id);
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Feedback deleted successfully"));
    }
}
