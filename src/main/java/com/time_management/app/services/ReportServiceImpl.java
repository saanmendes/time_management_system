package com.time_management.app.services;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.app.exceptions.ReportGenerationException;
import com.time_management.app.ports.ReportService;
import com.time_management.app.services.stackspot.QuickCommandService;
import com.time_management.app.services.stackspot.StackspotAuthenticator;
import com.time_management.domain.usecases.ReportUseCase;
import com.time_management.infra.input.mappers.ReportMapper;
import com.time_management.infra.output.entities.ReportEntity;
import com.time_management.infra.output.entities.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportUseCase reportUseCase;
    private final QuickCommandService quickCommandService;
    private final StackspotAuthenticator stackspotAuthenticator;

    public ReportServiceImpl(ReportUseCase reportUseCase, QuickCommandService quickCommandService, StackspotAuthenticator stackspotAuthenticator) {
        this.reportUseCase = reportUseCase;
        this.quickCommandService = quickCommandService;
        this.stackspotAuthenticator = stackspotAuthenticator;
    }

    @Override
    public ReportResponseDTO generateReport(int days) {
        try {
            ReportEntity reportEntity = createInitialReport(days);
            String accessToken = stackspotAuthenticator.authenticate();

            String optimizationResult = executeOptimization(accessToken, reportEntity.getTaskEntities());

            updateReportWithOptimizationResult(reportEntity, optimizationResult);

            return ReportMapper.reportEntityToReportResponseDTO(reportEntity);
        } catch (Exception e) {
            logger.error("Error generating report: {}", e.getMessage(), e);
            throw new ReportGenerationException("Failed to generate report", e);
        }
    }

    private ReportEntity createInitialReport(int days) {
        return ReportMapper.reportToReportEntity(reportUseCase.generateReport(days));
    }

    private String executeOptimization(String accessToken, List<TaskEntity> tasks) {
        String optimizationId = quickCommandService.executeOptimizationQuickCommand(accessToken, tasks);
        return fetchOptimizationResult(accessToken, optimizationId);
    }

    private String fetchOptimizationResult(String accessToken, String optimizationId) {
        String optimizationResult = quickCommandService.getQuickCommandOptimizationCallback(accessToken, optimizationId);

        while (optimizationResult == null) {
            logger.info("Quick command callback returned null, retrying...");
            optimizationResult = quickCommandService.getQuickCommandCategoryCallback(accessToken, optimizationId);
        }

        logger.info("Optimization Result: {}", optimizationResult);
        return optimizationResult;
    }

    private void updateReportWithOptimizationResult(ReportEntity reportEntity, String optimizationResult) {
        reportEntity.setSuggestion(optimizationResult);
    }
}