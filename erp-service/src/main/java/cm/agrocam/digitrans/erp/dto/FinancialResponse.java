package cm.agrocam.digitrans.erp.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialResponse {
    private Long id;
    private String reference;
    private Double amount;
    private String type;
    private String description;
    private LocalDate transactionDate;
}
