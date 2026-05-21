package cm.agrocam.digitrans.bi.dto.downstream;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponse {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
}
