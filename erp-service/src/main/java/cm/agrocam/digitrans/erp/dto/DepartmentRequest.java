package cm.agrocam.digitrans.erp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequest {
    @NotBlank(message = "Department code cannot be blank")
    private String code;

    @NotBlank(message = "Department name cannot be blank")
    private String name;

    private String description;
}
