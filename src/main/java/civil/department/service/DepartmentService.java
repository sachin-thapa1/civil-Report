package civil.department.service;

import civil.department.entity.Department;
import civil.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public Department createDepartment(String name, String code, String description) {
        Department dept = Department.builder()
                .name(name)
                .code(code)
                .description(description)
                .isActive(true)
                .build();
        return departmentRepository.save(dept);
    }
    //get all departments
    public List <Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    //get department by id
    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found  " + id));
    }
}