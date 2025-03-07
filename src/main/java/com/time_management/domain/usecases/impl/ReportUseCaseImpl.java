package com.time_management.domain.usecases.impl;

import com.time_management.domain.models.Report;
import com.time_management.domain.models.Task;
import com.time_management.domain.usecases.ReportUseCase;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class ReportUseCaseImpl implements ReportUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ReportUseCaseImpl.class);

    private final TaskRepository taskRepository;

    public ReportUseCaseImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Report generateReport(int days) {
        try {
            List<Task> tasks = fetchTasksFromLastDays(days);
            return buildReport(days, tasks);
        } catch (Exception exception) {
            logError(days, exception);
            throw exception;
        }
    }

    private List<Task> fetchTasksFromLastDays(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return TaskMapper.taskEntityToTaskList(taskRepository.findByInitialDateGreaterThanEqual(startDate));
    }

    private Report buildReport(int days, List<Task> tasks) {
        Report report = new Report();
        report.setDescription(String.format("Productivity report for the last %d days", days));
        report.setIssueDate(LocalDateTime.now());
        report.setTasks(tasks);
        return report;
    }

    private void logError(int days, Exception exception) {
        logger.error("Error generating report for {} days: {}", days, exception.getMessage(), exception);
    }
}