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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
// import java.net.http.HttpHeaders;
import java.util.List;
// import java.net.http.HttpHeaders;


import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/pdf")
    public void exportFullReport(HttpServletResponse response) throws IOException {
    response.setContentType("text/csv");
    logger.info("Starting report data collation for Gokul Food Crafts....");
    response.setHeader("Content-Disposition", "attachment; filename=Gokul_Food_Crafts_Invoices_Stats.csv");
    logger.info("Report is getting downloaded and is being sent to client right now ..........");
    reportService.writeFullCsvReport(response.getWriter());
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> downloadPdfReport() {
        byte[] pdfBytes = reportService.generateFullPdfReport();

        HttpHeaders headers = new HttpHeaders();  // ✅ from Spring
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Gokul_Food_Crafts_Invoice_Stats_Complete_Business_Owner_Level.pdf");
        logger.debug("Witnessed by Anjitha.... Signed by Adithyan");
        logger.info("Report is getting downloaded and is being sent to React client right now ..........");
        headers.setContentLength(pdfBytes.length);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setPragma("public");
        headers.setExpires(0);
        headers.set("Content-Transfer-Encoding", "binary");
        headers.set("Content-Description", "File Transfer");
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
