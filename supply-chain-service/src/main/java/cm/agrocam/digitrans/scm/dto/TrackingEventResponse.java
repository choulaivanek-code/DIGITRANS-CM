package cm.agrocam.digitrans.scm.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingEventResponse {
    private Long id;
    private Long shipmentId;
    private String eventLocation;
    private String statusDescription;
    private LocalDateTime eventTime;
}
