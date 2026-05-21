package cm.agrocam.digitrans.scm.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {
    private Long id;
    private String trackingNumber;
    private WarehouseResponse originWarehouse;
    private String destinationAddress;
    private String status;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
}
