package com.time_management.app.exceptions;

public class ReportGenerationException extends RuntimeException {
    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ReportGenerationException(String erroAoGerarRelatório) {
    }
}
