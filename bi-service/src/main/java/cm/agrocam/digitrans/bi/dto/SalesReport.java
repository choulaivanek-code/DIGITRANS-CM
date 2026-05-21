package cm.agrocam.digitrans.bi.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReport {
    private Long totalOrders;
    private Double totalRevenue;
    private Double averageTicket;
    private Map<String, List<RestaurantSalesDTO>> topRestaurantsByCity;
}
