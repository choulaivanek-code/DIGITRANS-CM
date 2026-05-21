package cm.agrocam.digitrans.bi.dto.downstream;

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
    private String type; // REVENUE or EXPENSE
    private String description;
    private LocalDate transactionDate;
}
