package cm.agrocam.digitrans.erp.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRequest {
    @NotBlank(message = "Reference cannot be blank")
    private String reference;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Type must be either REVENUE or EXPENSE")
    @Pattern(regexp = "^(REVENUE|EXPENSE)$", message = "Type must be REVENUE or EXPENSE")
    private String type;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;
}
