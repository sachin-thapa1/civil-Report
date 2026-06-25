package civil.report.service;

import civil.department.entity.Department;
import civil.department.repository.DepartmentRepository;
import civil.report.entity.Report;
import civil.report.entity.ReportCategory;
import civil.report.entity.ReportStatus;
import civil.report.repository.ReportRepository;
import civil.user.entity.User;
import civil.user.entity.UserRole;
import civil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;  // ← ADD THIS

    public Report createReport(String title, String description, double latitude,
                               double longitude, ReportCategory category, String userId) {
        UUID uuid = UUID.fromString(userId);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Auto-assign department by category
        Department department = departmentRepository.findByCategory(category.name())
                .orElse(null);

        ReportStatus status = ReportStatus.SUBMITTED;
        if (department != null) {
            status = ReportStatus.ASSIGNED;  // Auto-routed to department
        }

        Report report = Report.builder()
                .title(title)
                .description(description)
                .latitude(latitude)
                .longitude(longitude)
                .category(category)
                .status(status)
                .user(user)
                .department(department)
                .build();

        return reportRepository.save(report);
    }

    public Report assignReport(UUID reportId, UUID officerId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));

        User officer = userRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found: " + officerId));

        if (officer.getRole() != UserRole.OFFICER) {
            throw new RuntimeException("User is not an officer: " + officerId);
        }

        report.setAssignedTo(officer);
        report.setStatus(ReportStatus.ASSIGNED);

        return reportRepository.save(report);
    }

    public Report updateStatus(UUID reportId, ReportStatus newStatus, UUID officerId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));

        // Verify officer is assigned to this report
        if (report.getAssignedTo() == null || !report.getAssignedTo().getId().equals(officerId)) {
            throw new RuntimeException("Not authorized to update this report");
        }

        report.setStatus(newStatus);
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(UUID reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));
    }

    public List<Report> getReportsForOfficer(UUID officerId) {
        return reportRepository.findByAssignedToId(officerId);
    }

    public List<Report> getReportsByUser(UUID userId) {
        return reportRepository.findByUserId(userId);
    }
}