package com.time_management.domain.usecases;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.domain.models.Report;

public interface ReportUseCase {
    Report generateReport(int days);
}
