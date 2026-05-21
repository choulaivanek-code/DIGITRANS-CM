package cm.agrocam.digitrans.crm.service;

import cm.agrocam.digitrans.crm.dto.OrderRequest;
import cm.agrocam.digitrans.crm.dto.OrderResponse;
import cm.agrocam.digitrans.crm.entity.Customer;
import cm.agrocam.digitrans.crm.entity.Order;
import cm.agrocam.digitrans.crm.entity.Restaurant;
import cm.agrocam.digitrans.crm.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.crm.repository.CustomerRepository;
import cm.agrocam.digitrans.crm.repository.OrderRepository;
import cm.agrocam.digitrans.crm.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        RestaurantRepository restaurantRepository,
                        CustomerService customerService,
                        RestaurantService restaurantService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return mapToResponse(order);
    }

    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating new order for customer: {} in restaurant: {}", request.getCustomerId(), request.getRestaurantId());
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + request.getRestaurantId()));

        Order order = Order.builder()
                .customer(customer)
                .restaurant(restaurant)
                .totalAmount(request.getTotalAmount())
                .status(request.getStatus())
                .orderDate(LocalDate.now())
                .build();

        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    public OrderResponse updateOrder(Long id, OrderRequest request) {
        log.info("Updating order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + request.getRestaurantId()));

        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(request.getStatus());

        Order updated = orderRepository.save(order);
        return mapToResponse(updated);
    }

    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    public OrderResponse mapToResponse(Order order) {
        if (order == null) return null;
        return OrderResponse.builder()
                .id(order.getId())
                .customer(customerService.mapToResponse(order.getCustomer()))
                .restaurant(restaurantService.mapToResponse(order.getRestaurant()))
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .build();
    }
}
