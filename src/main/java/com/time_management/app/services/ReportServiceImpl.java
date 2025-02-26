package com.time_management.app.services;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.app.exceptions.ReportGenerationException;
import com.time_management.app.ports.ReportService;
import com.time_management.domain.usecases.ReportUseCase;
import com.time_management.infra.input.mappers.ReportMapper;
import com.time_management.infra.output.entities.ReportEntity;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportUseCase reportUseCase;

    public ReportServiceImpl(ReportUseCase reportUseCase) {
        this.reportUseCase = reportUseCase;
    }

    public ReportResponseDTO generateReport(int days) {
        try {
            ReportEntity reportEntity = ReportMapper.reportToReportEntity(reportUseCase.generateReport(days));
            return ReportMapper.reportEntityToReportResponseDTO(reportEntity);
        } catch (RuntimeException exception) {
            throw new ReportGenerationException("Failed to generate report for " + days + " days: " + exception.getMessage(), exception);
        }
    }

}