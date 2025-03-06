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
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

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
            // Gerar o relatório inicial
            ReportEntity reportEntity = ReportMapper.reportToReportEntity(reportUseCase.generateReport(days));

            // Obter o token de autenticação
            String accessToken = stackspotAuthenticator.authenticate();

            List<TaskEntity> tasks = reportEntity.getTaskEntities();

            String optimizationId = quickCommandService.executeOptimizationQuickCommand(accessToken, tasks);

            // Executar o Quick Command de otimização
            String optimizationResult = quickCommandService.getQuickCommandOptimizationCallback(accessToken, optimizationId);

            while (optimizationResult == null) {
                logger.info("Quick command callback returned null");
                optimizationResult = quickCommandService
                        .getQuickCommandCategoryCallback(accessToken, optimizationId);
            }

            // Logar o resultado da otimização
            logger.info("Optimization Result: {}", optimizationResult);

            // Atualizar o relatório com os resultados da otimização (se necessário)
            reportEntity.setSuggestion(optimizationResult);

            // Aqui você pode adicionar lógica para processar o resultado da otimização e atualizar o relatório

            // Converter o relatório para o DTO de resposta
            return ReportMapper.reportEntityToReportResponseDTO(reportEntity);
        } catch (Exception e) {
            logger.error("Error generating report: {}", e.getMessage(), e);
            throw new ReportGenerationException("Failed to generate report", e);
        }
    }
}