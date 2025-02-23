package com.time_management.infra.output.entities;

import com.time_management.domain.models.Suggestion;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report")
public class ReportEntity {
    @Id
    @UUID
    private String id;
    private String description;
    private LocalDateTime issueDate;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> taskEntities = new ArrayList<>();

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion.toString();
    }

    public Suggestion getSuggestion() {
        return new Suggestion();
    }

    public @UUID String getId() {
        return id;
    }

    public void setId(@UUID String id) {
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

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public void setTaskEntities(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }
}
