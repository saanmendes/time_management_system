package com.time_management.app.dtos.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class TaskRequestDTO {

    @NotNull(message = "email cannot be null")
    @NotBlank(message = "email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@zup\\.com\\.br$", message = "required format: {example}@zup.com.br")
    private String email;

    @NotNull(message = "description cannot be null")
    @NotBlank(message = "description cannot be blank")
    private String description;

    @NotNull(message = "initial date is a required field")
    private LocalDateTime initialDate;

    @NotNull(message = "end date is a required field")
    private LocalDateTime endDate;

    @NotNull(message = "role cannot be null")
    private String role;

    @NotNull(message = "task pending cannot be null")
    private boolean completed;

    public TaskRequestDTO() {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
