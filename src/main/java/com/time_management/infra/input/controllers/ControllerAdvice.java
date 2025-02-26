package com.time_management.infra.input.controllers;

import com.time_management.app.exceptions.ReportGenerationException;
import com.time_management.app.exceptions.TaskCreationException;
import com.time_management.app.exceptions.TaskDeletionException;
import com.time_management.app.exceptions.TaskNotFoundException;
import com.time_management.app.exceptions.TaskUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("field", fieldError.getField());
                    errorMap.put("message", fieldError.getDefaultMessage());
                    return errorMap;
                })
                .collect(Collectors.toList());

        logger.warn("Validation errors: {}", errors);

        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTaskNotFound(TaskNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());

        logger.warn("Task not found: {}", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskCreationException.class)
    public ResponseEntity<Map<String, String>> handleTaskCreationException(TaskCreationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Failed to create task: " + ex.getMessage());

        logger.error("Task creation failed: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskUpdateException.class)
    public ResponseEntity<Map<String, String>> handleTaskUpdateException(TaskUpdateException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Failed to update task: " + ex.getMessage());

        logger.error("Task update failed: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskDeletionException.class)
    public ResponseEntity<Map<String, String>> handleTaskDeletionException(TaskDeletionException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Failed to delete task: " + ex.getMessage());

        logger.error("Task deletion failed: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReportGenerationException.class)
    public ResponseEntity<Map<String, String>> handleReportGenerationException(ReportGenerationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Failed to generate report: " + ex.getMessage());

        logger.error("Report generation failed: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "An unexpected error occurred: " + ex.getMessage());

        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}