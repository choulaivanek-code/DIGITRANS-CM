package cm.agrocam.digitrans.scm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(name = "event_location", nullable = false)
    private String eventLocation;

    @Column(name = "status_description", nullable = false)
    private String statusDescription;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;
}
