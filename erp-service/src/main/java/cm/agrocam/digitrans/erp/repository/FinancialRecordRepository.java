package cm.agrocam.digitrans.erp.repository;

import cm.agrocam.digitrans.erp.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    Optional<FinancialRecord> findByReference(String reference);
}
