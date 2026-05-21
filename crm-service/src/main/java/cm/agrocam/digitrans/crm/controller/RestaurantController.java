package cm.agrocam.digitrans.crm.controller;

import cm.agrocam.digitrans.crm.dto.ApiResponse;
import cm.agrocam.digitrans.crm.dto.RestaurantRequest;
import cm.agrocam.digitrans.crm.dto.RestaurantResponse;
import cm.agrocam.digitrans.crm.service.RestaurantService;
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
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant Information Interface", description = "Endpoints for managing and viewing SavoirManger dining locations")
@PreAuthorize("hasAnyRole('MANAGER', 'STAFF')")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    @Operation(summary = "Get all restaurants", description = "Retrieve list of all active SavoirManger restaurant outlets")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAllRestaurants() {
        log.info("REST request to fetch all restaurants");
        List<RestaurantResponse> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(ApiResponse.success(restaurants, "Restaurants retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID", description = "Retrieve outlet details for a specific ID")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getRestaurantById(@PathVariable Long id) {
        log.info("REST request to fetch restaurant by ID: {}", id);
        RestaurantResponse restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(ApiResponse.success(restaurant, "Restaurant retrieved successfully"));
    }

    @PostMapping
    @Operation(summary = "Create restaurant", description = "Register a new dining outlet location")
    public ResponseEntity<ApiResponse<RestaurantResponse>> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        log.info("REST request to create restaurant: {}", request.getName());
        RestaurantResponse restaurant = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(restaurant, "Restaurant created successfully"));
    }
}
