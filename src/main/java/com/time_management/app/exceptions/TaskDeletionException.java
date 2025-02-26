package com.time_management.app.exceptions;

public class TaskDeletionException extends RuntimeException {
    public TaskDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
