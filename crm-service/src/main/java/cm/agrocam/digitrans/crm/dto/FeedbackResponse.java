package cm.agrocam.digitrans.crm.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {
    private Long id;
    private CustomerResponse customer;
    private Long orderId;
    private Integer rating;
    private String comment;
    private LocalDate submittedDate;
}
