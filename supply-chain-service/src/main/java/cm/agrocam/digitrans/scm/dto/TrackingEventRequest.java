package cm.agrocam.digitrans.scm.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingEventRequest {
    private Long id; // Required for offline sync

    @NotNull(message = "Shipment ID is required")
    private Long shipmentId;

    @NotBlank(message = "Event location is required")
    private String eventLocation;

    @NotBlank(message = "Status description is required")
    private String statusDescription;

    @NotNull(message = "Event time is required")
    private LocalDateTime eventTime;
}
