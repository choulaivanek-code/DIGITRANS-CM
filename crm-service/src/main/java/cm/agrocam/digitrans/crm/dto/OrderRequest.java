package cm.agrocam.digitrans.crm.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private Double totalAmount;

    @NotBlank(message = "Status must be PENDING, COMPLETED or CANCELLED")
    @Pattern(regexp = "^(PENDING|COMPLETED|CANCELLED)$", message = "Status must be PENDING, COMPLETED or CANCELLED")
    private String status;
}
