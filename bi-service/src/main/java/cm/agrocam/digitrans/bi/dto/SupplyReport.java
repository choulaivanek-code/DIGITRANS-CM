package cm.agrocam.digitrans.bi.dto;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyReport {
    private Map<String, Integer> stocksByWarehouse;
    private Long activeShipmentsCount;
    private Double deliverySuccessRate;
}
