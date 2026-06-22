package civil.report.dto;

import lombok.Data;

@Data
public class AssignReportRequest {
    private String officerId;  // UUID of officer to assign
}