package com.time_management.infra.input.controllers;

import com.time_management.app.ports.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/productivity")
    public ResponseEntity<?> generateProductivityReport(@RequestParam int days) {
        return ResponseEntity.ok(reportService.generateReport(days));
    }
}
