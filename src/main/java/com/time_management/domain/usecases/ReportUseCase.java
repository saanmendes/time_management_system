package com.time_management.domain.usecases;

import com.time_management.app.dtos.reports.ReportResponseDTO;

public interface ReportUseCase {
    ReportResponseDTO generateReport(int days);
}
