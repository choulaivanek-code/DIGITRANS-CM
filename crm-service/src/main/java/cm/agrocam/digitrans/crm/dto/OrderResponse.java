package cm.agrocam.digitrans.crm.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private CustomerResponse customer;
    private RestaurantResponse restaurant;
    private Double totalAmount;
    private String status;
    private LocalDate orderDate;
}
