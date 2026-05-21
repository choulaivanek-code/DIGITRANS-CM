package cm.agrocam.digitrans.erp.service;

import cm.agrocam.digitrans.erp.dto.FinancialRequest;
import cm.agrocam.digitrans.erp.dto.FinancialResponse;
import cm.agrocam.digitrans.erp.entity.FinancialRecord;
import cm.agrocam.digitrans.erp.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.erp.repository.FinancialRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FinancialRecordService {

    private static final Logger log = LoggerFactory.getLogger(FinancialRecordService.class);
    private final FinancialRecordRepository financialRecordRepository;

    public FinancialRecordService(FinancialRecordRepository financialRecordRepository) {
        this.financialRecordRepository = financialRecordRepository;
    }

    @Transactional(readOnly = true)
    public List<FinancialResponse> getAllFinancialRecords() {
        log.info("Fetching all financial records");
        return financialRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FinancialResponse getFinancialRecordById(Long id) {
        log.info("Fetching financial record by ID: {}", id);
        FinancialRecord rec = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record not found with ID: " + id));
        return mapToResponse(rec);
    }

    public FinancialResponse createFinancialRecord(FinancialRequest request) {
        log.info("Creating new financial record with reference: {}", request.getReference());
        FinancialRecord record = FinancialRecord.builder()
                .reference(request.getReference())
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .build();
        FinancialRecord saved = financialRecordRepository.save(record);
        return mapToResponse(saved);
    }

    public FinancialResponse updateFinancialRecord(Long id, FinancialRequest request) {
        log.info("Updating financial record with ID: {}", id);
        FinancialRecord rec = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record not found with ID: " + id));

        rec.setReference(request.getReference());
        rec.setAmount(request.getAmount());
        rec.setType(request.getType());
        rec.setDescription(request.getDescription());
        rec.setTransactionDate(request.getTransactionDate());

        FinancialRecord updated = financialRecordRepository.save(rec);
        return mapToResponse(updated);
    }

    public void deleteFinancialRecord(Long id) {
        log.info("Deleting financial record with ID: {}", id);
        if (!financialRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Financial record not found with ID: " + id);
        }
        financialRecordRepository.deleteById(id);
    }

    private FinancialResponse mapToResponse(FinancialRecord rec) {
        if (rec == null) return null;
        return FinancialResponse.builder()
                .id(rec.getId())
                .reference(rec.getReference())
                .amount(rec.getAmount())
                .type(rec.getType())
                .description(rec.getDescription())
                .transactionDate(rec.getTransactionDate())
                .build();
    }
}
