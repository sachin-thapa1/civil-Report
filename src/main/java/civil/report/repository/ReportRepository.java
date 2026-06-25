package civil.report.repository;

import civil.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByAssignedToId(UUID officerId);
    List<Report> findByUserId(UUID userId);

}