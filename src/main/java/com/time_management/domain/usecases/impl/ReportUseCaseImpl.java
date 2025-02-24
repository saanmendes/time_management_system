package com.time_management.domain.usecases.impl;

import com.time_management.app.dtos.reports.ReportResponseDTO;
import com.time_management.domain.models.Report;
import com.time_management.domain.models.Task;
import com.time_management.domain.usecases.ReportUseCase;
import com.time_management.infra.input.mappers.ReportMapper;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ReportUseCaseImpl implements ReportUseCase {

    private TaskRepository taskRepository;

    public ReportUseCaseImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ReportResponseDTO generateReport(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        List<Task> tasks = TaskMapper.taskEntityToTaskList(taskRepository.findByInitialDate(startDate));

        Report report = new Report();
        report.setDescription("Productivity report for the last " + days + " days");
        report.setIssueDate(startDate);
        report.setTasks(tasks);

        return ReportMapper.reportToReportResponseDTO(report);
    }

}
