package com.example.billing.controller;

import com.example.billing.model.Customer;
import com.example.billing.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.billing.model.Customer;
import com.example.billing.repository.CustomerRepository;
import com.example.billing.service.ReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/export")
public void exportFullReport(HttpServletResponse response) throws IOException {
    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=full_report.csv");

    reportService.writeFullCsvReport(response.getWriter());
}
}
