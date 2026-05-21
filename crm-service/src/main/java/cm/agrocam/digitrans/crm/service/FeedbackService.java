package cm.agrocam.digitrans.crm.service;

import cm.agrocam.digitrans.crm.dto.FeedbackRequest;
import cm.agrocam.digitrans.crm.dto.FeedbackResponse;
import cm.agrocam.digitrans.crm.entity.Customer;
import cm.agrocam.digitrans.crm.entity.Feedback;
import cm.agrocam.digitrans.crm.entity.Order;
import cm.agrocam.digitrans.crm.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.crm.repository.CustomerRepository;
import cm.agrocam.digitrans.crm.repository.FeedbackRepository;
import cm.agrocam.digitrans.crm.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);
    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           CustomerRepository customerRepository,
                           OrderRepository orderRepository,
                           CustomerService customerService) {
        this.feedbackRepository = feedbackRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbacks() {
        log.info("Fetching all feedbacks");
        return feedbackRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(Long id) {
        log.info("Fetching feedback by ID: {}", id);
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));
        return mapToResponse(feedback);
    }

    public FeedbackResponse createFeedback(FeedbackRequest request) {
        log.info("Creating new feedback for customer: {} on order: {}", request.getCustomerId(), request.getOrderId());
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.getOrderId()));

        Feedback feedback = Feedback.builder()
                .customer(customer)
                .order(order)
                .rating(request.getRating())
                .comment(request.getComment())
                .submittedDate(LocalDate.now())
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        return mapToResponse(saved);
    }

    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        log.info("Updating feedback with ID: {}", id);
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.getOrderId()));

        feedback.setCustomer(customer);
        feedback.setOrder(order);
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());

        Feedback updated = feedbackRepository.save(feedback);
        return mapToResponse(updated);
    }

    public void deleteFeedback(Long id) {
        log.info("Deleting feedback with ID: {}", id);
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with ID: " + id);
        }
        feedbackRepository.deleteById(id);
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {
        if (feedback == null) return null;
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .customer(customerService.mapToResponse(feedback.getCustomer()))
                .orderId(feedback.getOrder().getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .submittedDate(feedback.getSubmittedDate())
                .build();
    }
}
