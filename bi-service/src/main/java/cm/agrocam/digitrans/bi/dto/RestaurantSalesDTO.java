package cm.agrocam.digitrans.bi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantSalesDTO {
    private Long restaurantId;
    private String restaurantName;
    private Double sales;
    private Long orderCount;
}
