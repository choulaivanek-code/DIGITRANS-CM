package cm.agrocam.digitrans.bi.dto.downstream;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponse {
    private Long id;
    private String name;
    private String city;
    private String address;
    private String phone;
}
