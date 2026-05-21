package cm.agrocam.digitrans.scm.repository;

import cm.agrocam.digitrans.scm.entity.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {
    List<TrackingEvent> findByShipmentId(Long shipmentId);
}
