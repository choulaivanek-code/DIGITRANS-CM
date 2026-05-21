package cm.agrocam.digitrans.scm.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncPacket {
    private List<ShipmentRequest> shipments;
    private List<TrackingEventRequest> trackingEvents;
}
