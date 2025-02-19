package com.time_management.domain.dtos.reports;

import com.time_management.domain.models.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReportResponseDTO {
    private String id;
    private String description;
    private LocalDateTime issueDate;
    private SuggestionDTO suggestion;
    private List<Task> tasks;

    public ReportResponseDTO() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public SuggestionDTO getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionDTO suggestion) {
        this.suggestion = suggestion;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
