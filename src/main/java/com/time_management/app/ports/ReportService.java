package com.time_management.app.ports;

import com.time_management.app.dtos.reports.ReportResponseDTO;

public interface ReportService {

    ReportResponseDTO generateReport(int days);
}
