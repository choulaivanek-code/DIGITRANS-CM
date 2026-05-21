package cm.agrocam.digitrans.crm.service;

import cm.agrocam.digitrans.crm.dto.RestaurantRequest;
import cm.agrocam.digitrans.crm.dto.RestaurantResponse;
import cm.agrocam.digitrans.crm.entity.Restaurant;
import cm.agrocam.digitrans.crm.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.crm.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        log.info("Fetching all restaurants");
        return restaurantRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long id) {
        log.info("Fetching restaurant by ID: {}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + id));
        return mapToResponse(restaurant);
    }

    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        log.info("Creating new restaurant with name: {}", request.getName());
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .city(request.getCity())
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();
        Restaurant saved = restaurantRepository.save(restaurant);
        return mapToResponse(saved);
    }

    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest request) {
        log.info("Updating restaurant with ID: {}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + id));

        restaurant.setName(request.getName());
        restaurant.setCity(request.getCity());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());

        Restaurant updated = restaurantRepository.save(restaurant);
        return mapToResponse(updated);
    }

    public void deleteRestaurant(Long id) {
        log.info("Deleting restaurant with ID: {}", id);
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found with ID: " + id);
        }
        restaurantRepository.deleteById(id);
    }

    public RestaurantResponse mapToResponse(Restaurant restaurant) {
        if (restaurant == null) return null;
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .city(restaurant.getCity())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .build();
    }
}
