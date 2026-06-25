package civil.department.controller;

import civil.department.entity.Department;
import civil.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // POST /api/v1/departments
    @PostMapping
    public ResponseEntity<Department> createDepartment(
            @RequestParam String name,
            @RequestParam String code,
            @RequestParam(required = false) String description) {

        Department dept = departmentService.createDepartment(name, code, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(dept);
    }

    // GET /api/v1/departments
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
}