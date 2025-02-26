package com.time_management.app.exceptions;

public class TaskUpdateException extends RuntimeException {
    public TaskUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
