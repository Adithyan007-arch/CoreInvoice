package com.example.billing.service;

import com.example.billing.model.Customer;
import com.example.billing.repository.CustomerRepository;
import com.example.billing.repository.InvoiceRepository;
import com.example.billing.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public ReportService(CustomerRepository customerRepository,
                         ProductRepository productRepository,
                         InvoiceRepository invoiceRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public void writeFullCsvReport(PrintWriter writer) {
    writer.println("=== Customers ===");
    writer.println("ID,Name,Email");
    customerRepository.findAll().forEach(c ->
        writer.printf("%d,%s,%s%n", c.getId(), c.getName(), c.getEmail())
    );

    writer.println("\n=== Products ===");
    writer.println("ID,Name,Category,Price");
    productRepository.findAll().forEach(p ->
        writer.printf("%d,%s,%s,%.2f%n", p.getId(), p.getName(), p.getCategory(), p.getPrice())
    );

    writer.println("\n=== Invoices ===");
    writer.println("ID,CustomerID,Total,Items,Date");
    invoiceRepository.findAll().forEach(i ->
        writer.printf("%d,%d,%.2f,%s%n", i.getId(), i.getCustomer().getId(), i.getTotalAmount(), i.getItems(), i.getDate())
    );
}

}