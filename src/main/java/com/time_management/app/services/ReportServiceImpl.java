package com.time_management.app.services;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.app.dtos.reports.SuggestionDTO;
import com.time_management.app.exceptions.ReportGenerationException;
import com.time_management.app.ports.ReportService;
import com.time_management.app.services.stackspot.QuickCommandService;
import com.time_management.app.services.stackspot.StackspotAuthenticator;
import com.time_management.domain.usecases.ReportUseCase;
import com.time_management.infra.input.mappers.ReportMapper;
import com.time_management.infra.output.entities.ReportEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportUseCase reportUseCase;
    private final QuickCommandService quickCommandService;
    private final StackspotAuthenticator stackspotAuthenticator;

    public ReportServiceImpl(ReportUseCase reportUseCase, QuickCommandService quickCommandService, StackspotAuthenticator stackspotAuthenticator) {
        this.quickCommandService = quickCommandService;
        this.reportUseCase = reportUseCase;
        this.stackspotAuthenticator = stackspotAuthenticator;
    }

    public ReportResponseDTO generateReport(int days) {
        try {
            ReportEntity reportEntity = ReportMapper.reportToReportEntity(reportUseCase.generateReport(days));

            String token = stackspotAuthenticator.authenticate();
            logger.info("Stackspot authenticated successfully: {}", token);

            String executionId = quickCommandService.executeOptimizationQuickCommand(token, reportEntity);
            logger.info("Quick command executed successfully");

            SuggestionDTO optimization = quickCommandService.getQuickCommandCallbackAsDTO(token, executionId);

            while (optimization == null) {
                logger.info("Quick command callback returned null");
                optimization = quickCommandService
                        .getQuickCommandCallbackAsDTO(token, executionId);
            }
            logger.info("Quick command callback returned: {}", optimization);

            return ReportMapper.reportEntityToReportResponseDTO(reportEntity);
        } catch (RuntimeException exception) {
            logger.error("Error generating report for {} days: " + exception.getMessage(), days, exception);
            throw new ReportGenerationException("Failed to generate report for " + days + " days: " + exception.getMessage(), exception);
        }
    }

}