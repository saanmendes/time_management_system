package com.time_management.app.dtos.reports;

import com.time_management.domain.models.Task;

import java.util.List;

public class SuggestionDTO {
    private List<Task> tasks;
    private String review;
    private List<String> optimization;

    public SuggestionDTO() {
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public List<String> getOptimization() {
        return optimization;
    }

    public void setOptimization(List<String> optimization) {
        this.optimization = optimization;
    }
}
