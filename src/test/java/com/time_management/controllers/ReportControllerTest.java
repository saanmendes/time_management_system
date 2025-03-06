package com.time_management.controllers;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.app.ports.ReportService;
import com.time_management.infra.input.controllers.ReportController;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    public ReportControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGenerateProductivityReportSuccessfully()  {
        final var days = 7;
        final var reportResponseDTO = Instancio.create(ReportResponseDTO.class);

        when(reportService.generateReport(days)).thenReturn(reportResponseDTO);

        ResponseEntity<?> response = reportController.generateProductivityReport(days);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(reportResponseDTO);
        verify(reportService).generateReport(days);
    }
}
