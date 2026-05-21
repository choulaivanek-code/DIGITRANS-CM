package cm.agrocam.digitrans.bi.dto.downstream;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Double salary;
    private String role;
    private LocalDate hireDate;
    private DepartmentResponse department;
}
