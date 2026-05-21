package cm.agrocam.digitrans.scm.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentRequest {
    private Long id; // Required for offline sync (optional for normal REST creation)

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotNull(message = "Origin Warehouse ID is required")
    private Long originWarehouseId;

    @NotBlank(message = "Destination address is required")
    private String destinationAddress;

    @NotBlank(message = "Status must be SHIPPED, DELIVERED or IN_TRANSIT")
    @Pattern(regexp = "^(SHIPPED|DELIVERED|IN_TRANSIT)$", message = "Status must be SHIPPED, DELIVERED or IN_TRANSIT")
    private String status;

    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    private LocalDate arrivalDate;
}
