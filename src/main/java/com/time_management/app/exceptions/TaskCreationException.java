package com.time_management.app.exceptions;

public class TaskCreationException extends RuntimeException {
    public TaskCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
