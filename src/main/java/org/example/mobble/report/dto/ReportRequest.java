package org.example.mobble.report.dto;

import lombok.Data;
import org.example.mobble.report.domain.ReportStatus;

public class ReportRequest {
    @Data
    public class ReportUpateDTO{

    private ReportStatus status;
}
}
