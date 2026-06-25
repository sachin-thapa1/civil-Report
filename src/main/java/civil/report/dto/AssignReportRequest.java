package civil.report.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AssignReportRequest {
    private UUID officerId;
}