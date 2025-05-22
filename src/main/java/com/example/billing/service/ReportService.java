package com.example.billing.service;

import com.example.billing.model.Invoice;
import com.example.billing.model.InvoiceItem;
import com.example.billing.repository.CustomerRepository;
import com.example.billing.repository.InvoiceRepository;
import com.example.billing.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.awt.Color;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

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
        // writer.println("=== Customers ===");
        // writer.println("ID,Name,Email");
        // customerRepository.findAll().forEach(c ->
        //     writer.printf("%d,%s,%s%n", c.getId(), c.getName(), c.getEmail())
        // );

        // writer.println("\n=== Products ===");
        // writer.println("ID,Name,Category,Price");
        // productRepository.findAll().forEach(p ->
        //     writer.printf("%d,%s,%s,%.2f%n", p.getId(), p.getName(), p.getCategory(), p.getPrice())
        // );

        // writer.println("\n=== Invoices ===");
        writer.println("ID,CustomerName,Total,Items,Date");

        invoiceRepository.findAll().forEach(i -> {
            String itemsDescription = i.getItems().stream()
                .map(item -> {
                    String productName = item.getProduct().getName();
                    int qty = item.getQuantity();
                    return productName + qty;
                })
                .collect(Collectors.joining(", "));  // Use semicolon to separate items

            writer.printf("%d,\"%s\",%.2f,\"%s\",%s%n",
                i.getId(),
                i.getCustomer().getName(),
                i.getTotalAmount(),
                itemsDescription,
                i.getDate()
            );
        });
    }

    public byte[] generateFullPdfReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate()); // landscape for wide tables
            PdfWriter.getInstance(document, out);
            document.open();
    
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        try {
            Image logo = Image.getInstance("src/main/resources/static/logo.png"); // Adjust path accordingly
            logo.scaleAbsolute(100, 50);
            logo.setAlignment(Image.ALIGN_LEFT);
            document.add(logo);
        } catch (Exception e) {
            System.err.println("Logo not found or failed to load.");
        }
    
            // Title
            Paragraph title = new Paragraph("Invoice Summary Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
    
            // Table
            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 3, 3, 3, 3, 3, 5, 5, 5});
    
            Stream.of("ID", "Customer Name", "CGST Percentage", "SGST Percentage", "IGST Percentage", "Price" ,"Total Amount", "Items", "Item Number", "Date")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                        cell.setBackgroundColor(Color.LIGHT_GRAY);
                        table.addCell(cell);
                    });
    
            List<Invoice> invoices = invoiceRepository.findAll();
    
            for (Invoice i : invoices) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(i.getId()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(i.getCustomer().getName(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(i.getItems().get(0).getProduct().getCgstPercentage(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(i.getItems().get(0).getProduct().getSgstPercentage(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(i.getItems().get(0).getProduct().getIgstPercentage(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", i.getItems().get(0).getProduct().getPrice()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", i.getTotalAmount()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(i.getItems().get(0).getProduct().getName(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(i.getItems().get(0).getQuantity()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(i.getDate().toString(), normalFont)));
            }
    
            document.add(table);
            document.close();
    
            return out.toByteArray();
    
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }
}