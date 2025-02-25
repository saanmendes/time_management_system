package com.time_management.infra.input.mappers;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.domain.models.Report;
import com.time_management.domain.models.Task;
import com.time_management.infra.output.entities.ReportEntity;

public class ReportMapper {

    public static ReportResponseDTO reportEntityToReportResponseDTO(ReportEntity reportEntity) {
        ReportResponseDTO reportResponseDTO = new ReportResponseDTO();
        reportResponseDTO.setId(reportEntity.getId());
        reportResponseDTO.setIssueDate(reportEntity.getIssueDate());
        reportResponseDTO.setDescription(reportEntity.getDescription());
        reportResponseDTO.setTasks(TaskMapper.taskEntityToTaskList(reportEntity.getTaskEntities()));
        return reportResponseDTO;
    }

    public static ReportEntity reportToReportEntity(Report report) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setId(report.getId());
        reportEntity.setIssueDate(report.getIssueDate());
        reportEntity.setDescription(report.getDescription());
        reportEntity.setTaskEntities(TaskMapper.taskToTaskEntityList(report.getTasks()));
        return reportEntity;
    }

    public static Report reportEntityToReport(ReportEntity reportEntity) {
        Report report = new Report();
        report.setId(reportEntity.getId());
        report.setIssueDate(reportEntity.getIssueDate());
        report.setDescription(reportEntity.getDescription());
        return report;
    }

    public static ReportResponseDTO reportToReportResponseDTO(Report report) {
        ReportResponseDTO reportResponseDTO = new ReportResponseDTO();
        reportResponseDTO.setId(report.getId());
        reportResponseDTO.setIssueDate(report.getIssueDate());
        reportResponseDTO.setDescription(report.getDescription());
        reportResponseDTO.setTasks(report.getTasks());
        return reportResponseDTO;
    }
}
