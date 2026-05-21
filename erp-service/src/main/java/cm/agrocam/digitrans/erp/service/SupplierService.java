package cm.agrocam.digitrans.erp.service;

import cm.agrocam.digitrans.erp.dto.SupplierRequest;
import cm.agrocam.digitrans.erp.dto.SupplierResponse;
import cm.agrocam.digitrans.erp.entity.Supplier;
import cm.agrocam.digitrans.erp.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.erp.repository.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {

    private static final Logger log = LoggerFactory.getLogger(SupplierService.class);
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        log.info("Fetching all suppliers");
        return supplierRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        log.info("Fetching supplier by ID: {}", id);
        Supplier sup = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        return mapToResponse(sup);
    }

    public SupplierResponse createSupplier(SupplierRequest request) {
        log.info("Creating new supplier with Tax ID: {}", request.getTaxId());
        Supplier supplier = Supplier.builder()
                .companyName(request.getCompanyName())
                .contactName(request.getContactName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .taxId(request.getTaxId())
                .build();
        Supplier saved = supplierRepository.save(supplier);
        return mapToResponse(saved);
    }

    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier with ID: {}", id);
        Supplier sup = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        sup.setCompanyName(request.getCompanyName());
        sup.setContactName(request.getContactName());
        sup.setEmail(request.getEmail());
        sup.setPhone(request.getPhone());
        sup.setAddress(request.getAddress());
        sup.setTaxId(request.getTaxId());

        Supplier updated = supplierRepository.save(sup);
        return mapToResponse(updated);
    }

    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with ID: {}", id);
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + id);
        }
        supplierRepository.deleteById(id);
    }

    private SupplierResponse mapToResponse(Supplier sup) {
        if (sup == null) return null;
        return SupplierResponse.builder()
                .id(sup.getId())
                .companyName(sup.getCompanyName())
                .contactName(sup.getContactName())
                .email(sup.getEmail())
                .phone(sup.getPhone())
                .address(sup.getAddress())
                .taxId(sup.getTaxId())
                .build();
    }
}
