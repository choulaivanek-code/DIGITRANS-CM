package cm.agrocam.digitrans.erp.service;

import cm.agrocam.digitrans.erp.dto.DepartmentRequest;
import cm.agrocam.digitrans.erp.dto.DepartmentResponse;
import cm.agrocam.digitrans.erp.entity.Department;
import cm.agrocam.digitrans.erp.exception.ResourceNotFoundException;
import cm.agrocam.digitrans.erp.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        log.info("Fetching department by ID: {}", id);
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return mapToResponse(dept);
    }

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        log.info("Creating new department with code: {}", request.getCode());
        Department department = Department.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Department saved = departmentRepository.save(department);
        return mapToResponse(saved);
    }

    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        log.info("Updating department with ID: {}", id);
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        dept.setCode(request.getCode());
        dept.setName(request.getName());
        dept.setDescription(request.getDescription());

        Department updated = departmentRepository.save(dept);
        return mapToResponse(updated);
    }

    public void deleteDepartment(Long id) {
        log.info("Deleting department with ID: {}", id);
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }

    public DepartmentResponse mapToResponse(Department dept) {
        if (dept == null) return null;
        return DepartmentResponse.builder()
                .id(dept.getId())
                .code(dept.getCode())
                .name(dept.getName())
                .description(dept.getDescription())
                .build();
    }
}
