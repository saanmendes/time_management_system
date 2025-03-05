package com.time_management.services;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.app.exceptions.ReportGenerationException;
import com.time_management.app.ports.ReportService;
import com.time_management.app.services.ReportServiceImpl;
import com.time_management.app.services.stackspot.QuickCommandService;
import com.time_management.app.services.stackspot.StackspotAuthenticator;
import com.time_management.domain.usecases.ReportUseCase;
import com.time_management.infra.input.mappers.ReportMapper;
import com.time_management.infra.output.entities.ReportEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @Test
    void deveGerarRelatorioComSucesso() {
        // Arrange
        final var reportUseCase = mock(ReportUseCase.class);
        final var quickCommandService = mock(QuickCommandService.class);
        final var stackspotAuthenticator = mock(StackspotAuthenticator.class);
        final var reportService = new ReportServiceImpl(reportUseCase, quickCommandService, stackspotAuthenticator);

        final var report = Instancio.create(com.time_management.domain.models.Report.class); // Tipo esperado pelo use case
        final var reportEntity = Instancio.create(ReportEntity.class); // Tipo esperado pelo mapper
        final var reportResponseDTO = Instancio.create(ReportResponseDTO.class); // Tipo esperado como retorno
        final int dias = 7;

        // Stub para o método do use case
        when(reportUseCase.generateReport(dias)).thenReturn(report);

        // Mock estático para o mapper
        try (var mockedMapper = Mockito.mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.reportToReportEntity(report)).thenReturn(reportEntity);
            mockedMapper.when(() -> ReportMapper.reportEntityToReportResponseDTO(reportEntity)).thenReturn(reportResponseDTO);

            // Act
            final var resultado = reportService.generateReport(dias);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isSameAs(reportResponseDTO);

            mockedMapper.verify(() -> ReportMapper.reportToReportEntity(report), times(1));
            mockedMapper.verify(() -> ReportMapper.reportEntityToReportResponseDTO(reportEntity), times(1));
        }

        // Verifica se o método do use case foi chamado corretamente
        final var captor = ArgumentCaptor.forClass(Integer.class);
        verify(reportUseCase, times(1)).generateReport(captor.capture());
        assertThat(captor.getValue()).isEqualTo(dias);
    }

    @Test
    void deveLancarExcecaoQuandoGeracaoDeRelatorioFalhar() {
        // Arrange
        final var reportUseCase = mock(ReportUseCase.class);
        final var quickCommandService = mock(QuickCommandService.class);
        final var stackspotAuthenticator = mock(StackspotAuthenticator.class);
        final var reportService = new ReportServiceImpl(reportUseCase, quickCommandService, stackspotAuthenticator);

        final int dias = 7;

        // Stub para simular exceção
        when(reportUseCase.generateReport(dias)).thenThrow(new ReportGenerationException("Erro ao gerar relatório"));

        // Act & Assert
        try {
            reportService.generateReport(dias);
        } catch (ReportGenerationException e) {
            assertThat(e).isNotNull();
            assertThat(e.getMessage()).isEqualTo("Erro ao gerar relatório");
        }

        // Verifica se o método do use case foi chamado corretamente
        final var captor = ArgumentCaptor.forClass(Integer.class);
        verify(reportUseCase, times(1)).generateReport(captor.capture());
        assertThat(captor.getValue()).isEqualTo(dias);
    }
}