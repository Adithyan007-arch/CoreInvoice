package com.example.billing.controller;

import com.example.billing.dto.InvoiceRequest;
import com.example.billing.dto.InvoiceResponse;
import com.example.billing.model.Invoice;
import com.example.billing.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody InvoiceRequest request) {
        Invoice invoice = invoiceService.createInvoice(request);
        InvoiceResponse response = new InvoiceResponse(
                invoice.getId(),
                invoice.getCustomer(),
                invoice.getItems(),
                invoice.getDate(),
                invoice.isPaid(),
                invoice.getTotalAmount()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> responses = invoiceService.getAllInvoices()
                .stream()
                .map(invoice -> new InvoiceResponse(
                        invoice.getId(),
                        invoice.getCustomer(),
                        invoice.getItems(),
                        invoice.getDate(),
                        invoice.isPaid(),
                        invoice.getTotalAmount()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(invoice -> new InvoiceResponse(
                        invoice.getId(),
                        invoice.getCustomer(),
                        invoice.getItems(),
                        invoice.getDate(),
                        invoice.isPaid(),
                        invoice.getTotalAmount()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}