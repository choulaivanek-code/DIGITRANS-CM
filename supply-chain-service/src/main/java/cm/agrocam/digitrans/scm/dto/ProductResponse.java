package cm.agrocam.digitrans.scm.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String sku;
    private String category;
    private Double unitPrice;
    private Integer stockQuantity;
}
