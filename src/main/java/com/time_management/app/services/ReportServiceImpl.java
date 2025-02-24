package com.time_management.app.services;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.app.ports.ReportService;
import com.time_management.domain.usecases.ReportUseCase;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

   private final ReportUseCase reportUseCase;

   public ReportServiceImpl(ReportUseCase reportUseCase) {
       this.reportUseCase = reportUseCase;
   }

   public ReportResponseDTO generateReport(int days) {
       return reportUseCase.generateReport(days);
   }

}
