package com.example.billing.controller;

import com.example.billing.dto.InvoiceRequest;
import com.example.billing.dto.InvoiceResponse;
import com.example.billing.model.Customer;
import com.example.billing.model.Invoice;
import com.example.billing.model.InvoiceItem;
import com.example.billing.model.Product;
import com.example.billing.repository.InvoiceRepository;
import com.example.billing.repository.ProductRepository;
import com.example.billing.service.InvoiceService;

import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final ProductRepository productRepository;

    private final InvoiceRepository invoiceRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;

    InvoiceController(InvoiceRepository invoiceRepository, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
    }

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
        logger.info("New Invoice has been created for customer: " + invoice.getCustomer().getName() + " with Amount: " + invoice.getTotalAmount());
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
        logger.debug("About to fetch all invoices");
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        logger.debug("About to fetch Invoice with ID : " + id);
        return invoiceService.getInvoiceById(id)
                .map(invoice -> new InvoiceResponse(
                        invoice.getId(),
                        invoice.getCustomer(),
                        invoice.getItems(),
                        invoice.getDate(),
                        invoice.isPaid(),
                        invoice.getTotalAmount()
                ))
                .map(invoice -> {
                        logger.info("Successfully fetched Invoice for : " + invoiceService.getInvoiceById(id).get().getCustomer().getName() + " with ID: " + id);
                        InvoiceResponse invoiceResponse = new InvoiceResponse(
                                invoice.getId(),
                                invoice.getCustomer(),
                                invoice.getItems(),
                                invoice.getDate(),
                                invoice.isPaid(),
                                invoice.getTotalAmount()
                        );
                        return ResponseEntity.ok(invoiceResponse);
                })
                .orElseGet(() -> {
                    logger.warn("Unable to fetch the invoice for Customer : " + invoiceService.getInvoiceById(id).get().getCustomer().getName() + " with ID: " + id);    
                    return ResponseEntity.notFound().build();
                });
    }
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> update(@PathVariable Long id, @RequestBody Invoice updated) {
        return invoiceRepository.findById(id).map(existing -> {
            existing.setCustomer(updated.getCustomer());
            existing.setDate(updated.getDate());
            existing.setPaid(updated.isPaid());
            existing.setTotalAmount(updated.getTotalAmount());

            // Ensure each InvoiceItem is properly linked
            for (InvoiceItem item : updated.getItems()) {
                item.setInvoice(existing); // set back-reference
            }
            existing.setItems(updated.getItems());

            logger.info("Updating Invoice for Customer: " + existing.getCustomer().getName());
            return ResponseEntity.ok(invoiceRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + id));
                logger.info("Deleting Invoice for Customer : " + invoiceService.getInvoiceById(id).get().getCustomer().getName() + " from database position: " + id);
        invoiceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/export/{invoiceId}")
    public void exportSingleInvoice(@PathVariable Long invoiceId, HttpServletResponse response) throws IOException {
        logger.info("Exporting invoice for Customer : " + invoiceService.getInvoiceById(invoiceId).get().getCustomer().getName() + " with ID: " + invoiceId +  " to CSV");
    invoiceService.exportInvoiceToPDF(invoiceId, response);
}
}