package cm.agrocam.digitrans.bi.dto;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeReport {
    private Map<String, Long> staffCountByDepartment;
    private Double totalPayroll;
    private Double expenseToRevenueRatio;
}
