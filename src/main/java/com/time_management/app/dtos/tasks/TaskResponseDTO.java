package com.time_management.app.dtos.tasks;

import java.time.LocalDateTime;

public class TaskResponseDTO {
    private String id;
    private String email;
    private String description;
    private LocalDateTime initialDate;
    private LocalDateTime endDate;
    private String priority;
    private String role;
    private String category;
    private boolean pending;

    public TaskResponseDTO(String id, String email, String description, LocalDateTime initialDate, LocalDateTime endDate, String role, boolean pending) {
        this.id = id;
        this.email = email;
        this.description = description;
        this.initialDate = initialDate;
        this.endDate = endDate;
        this.role = role;
        this.pending = pending;
    }

    public TaskResponseDTO(String id, String email, String description, LocalDateTime initialDate, LocalDateTime endDate, String priority, String role, String category, boolean pending) {
        this.id = id;
        this.email = email;
        this.description = description;
        this.initialDate = initialDate;
        this.endDate = endDate;
        this.priority = priority;
        this.role = role;
        this.category = category;
        this.pending = pending;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
