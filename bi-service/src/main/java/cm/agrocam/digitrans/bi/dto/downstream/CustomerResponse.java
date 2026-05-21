package cm.agrocam.digitrans.bi.dto.downstream;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Integer loyaltyPoints;
    private LocalDate registeredDate;
}
