package com.time_management.services;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.app.exceptions.ReportGenerationException;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @Test
    void shouldGenerateReportSuccessfully() {
        final var reportUseCase = mock(ReportUseCase.class);
        final var quickCommandService = mock(QuickCommandService.class);
        final var stackspotAuthenticator = mock(StackspotAuthenticator.class);
        final var reportService = new ReportServiceImpl(reportUseCase, quickCommandService, stackspotAuthenticator);

        final var report = Instancio.create(com.time_management.domain.models.Report.class);
        final var reportEntity = Instancio.create(ReportEntity.class);
        final var reportResponseDTO = Instancio.create(ReportResponseDTO.class);
        final int days = 7;

        when(reportUseCase.generateReport(days)).thenReturn(report);
        try (var mockedMapper = Mockito.mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.reportToReportEntity(report)).thenReturn(reportEntity);
            mockedMapper.when(() -> ReportMapper.reportEntityToReportResponseDTO(reportEntity)).thenReturn(reportResponseDTO);

            final var result = reportService.generateReport(days);
            assertThat(result).isNotNull();
            assertThat(result).isSameAs(reportResponseDTO);

            mockedMapper.verify(() -> ReportMapper.reportToReportEntity(report), times(1));
            mockedMapper.verify(() -> ReportMapper.reportEntityToReportResponseDTO(reportEntity), times(1));
        }
        final var captor = ArgumentCaptor.forClass(Integer.class);
        verify(reportUseCase, times(1)).generateReport(captor.capture());
        assertThat(captor.getValue()).isEqualTo(days);
    }

    @Test
    void shouldThrowExceptionWhenReportGenerationFails() {
        final var reportUseCase = mock(ReportUseCase.class);
        final var quickCommandService = mock(QuickCommandService.class);
        final var stackspotAuthenticator = mock(StackspotAuthenticator.class);
        final var reportService = new ReportServiceImpl(reportUseCase, quickCommandService, stackspotAuthenticator);

        final int days = 7;
        when(reportUseCase.generateReport(days)).thenThrow(new ReportGenerationException("Error generating report"));

        assertThatThrownBy(() -> reportService.generateReport(days))
                .isInstanceOf(ReportGenerationException.class)
                .hasMessageContaining("Error generating report");

        final var captor = ArgumentCaptor.forClass(Integer.class);
        verify(reportUseCase, times(1)).generateReport(captor.capture());
        assertThat(captor.getValue()).isEqualTo(days);
    }
}