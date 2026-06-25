package civil.report.controller;

import civil.report.dto.AssignReportRequest;
import civil.report.dto.ReportRequestDto;
import civil.report.entity.Report;
import civil.report.entity.ReportStatus;
import civil.report.service.ReportService;
import civil.user.entity.User;
import civil.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Report> createReport(@Valid @RequestBody ReportRequestDto dto) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Report report = reportService.createReport(
                dto.getTitle(),
                dto.getDescription(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getCategory(),
                user.getId().toString()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{reportId}/assign")
    public ResponseEntity<Report> assignReport(
            @PathVariable UUID reportId,
            @RequestBody AssignReportRequest request) {

        Report report = reportService.assignReport(reportId, request.getOfficerId());
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable UUID id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/my-submissions")
    public ResponseEntity<List<Report>> getMySubmissions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(reportService.getReportsByUser(user.getId()));
    }

    @PreAuthorize("hasRole('OFFICER')")
    @PostMapping("/{reportId}/status")
    public ResponseEntity<Report> updateStatus(
            @PathVariable UUID reportId,
            @RequestBody Map<String, String> request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User officer = userRepository.findByEmail(email).orElseThrow();

        ReportStatus newStatus = ReportStatus.valueOf(request.get("status"));
        Report report = reportService.updateStatus(reportId, newStatus, officer.getId());

        return ResponseEntity.ok(report);
    }

    @PreAuthorize("hasRole('OFFICER')")
    @GetMapping("/my-reports")
    public ResponseEntity<List<Report>> getMyReports() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User officer = userRepository.findByEmail(email)
                .orElseThrow();

        return ResponseEntity.ok(
                reportService.getReportsForOfficer(officer.getId())
        );
    }
}