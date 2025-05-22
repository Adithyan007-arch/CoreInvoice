package com.example.billing.service;

import com.example.billing.dto.InvoiceRequest;
import com.example.billing.model.Customer;
import com.example.billing.model.Invoice;
import com.example.billing.model.InvoiceItem;
import com.example.billing.model.Product;
import com.example.billing.repository.CustomerRepository;
import com.example.billing.repository.InvoiceItemRepository;
import com.example.billing.repository.InvoiceRepository;
import com.example.billing.repository.ProductRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    public Invoice createInvoice(InvoiceRequest request) {
        Optional<Customer> optionalCustomer = customerRepository.findById(request.getCustomerId());
        if (optionalCustomer.isEmpty()) throw new RuntimeException("Customer not found");

        Invoice invoice = new Invoice();
        invoice.setCustomer(optionalCustomer.get());
        invoice.setDate(LocalDate.now());
        invoice.setPaid(request.isPaid());

        invoice = invoiceRepository.save(invoice);

        List<InvoiceItem> items = new ArrayList<>();
        double total = 0;

        for (InvoiceRequest.Item item : request.getItems()) {
            Optional<Product> optionalProduct = productRepository.findById(item.getProductId());
            if (optionalProduct.isEmpty()) continue;

            Product product = optionalProduct.get();
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProduct(product);
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setPrice(product.getPrice());
            invoiceItem.setInvoice(invoice);
            items.add(invoiceItem);

            total += product.getPrice() * item.getQuantity();
        }

        invoice.setTotalAmount(total);
        invoiceItemRepository.saveAll(items);

        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    private double safeGST(String percentageStr, double amount) {
        try {
            if (percentageStr != null && !percentageStr.trim().isEmpty()) {
                double percentage = Double.parseDouble(percentageStr.trim());
                return amount * (percentage / 100.0);
            }
        } catch (NumberFormatException ignored) {
            logger.error("This is a number format exception. Check into code for further clarity");
        }
        return 0.0;
    }

    public void exportInvoiceToPDF(Long invoiceId, HttpServletResponse response) throws IOException {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if (invoiceOpt.isEmpty()) throw new RuntimeException("Invoice not found");

        Invoice invoice = invoiceOpt.get();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Invoice_" + invoiceId + ".pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
        Paragraph title = new Paragraph("Gokul Food Crafts", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
        Paragraph subTitle = new Paragraph("Manufacturer Dairy Units", headerFont);
        subTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(subTitle);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph superTitle = new Paragraph("Vairankode Road Kuttur - 676551", normalFont);
        superTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(superTitle);
        Paragraph superTitle2 = new Paragraph("Phone: 9895243836", normalFont);
        superTitle2.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(superTitle2);
        //document.add(new Paragraph("Vairankode Road Kuttur - 676551", normalFont).setAlignment(Paragraph.ALIGN_CENTER));
        // document.add(new Paragraph("Phone: 9895243836", normalFont).setAlignment(Paragraph.ALIGN_CENTER));

        try {
            Image logo = Image.getInstance("src/main/resources/static/logo.png");
            logo.scaleAbsolute(100, 50);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            System.err.println("Logo load failed: " + e.getMessage());
        }

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Invoice No: " + invoice.getId(), normalFont));
        document.add(new Paragraph("Purchaser: " + invoice.getCustomer().getName(), normalFont));
        document.add(new Paragraph("Date: " + invoice.getDate(), normalFont));
        document.add(new Paragraph("Payment Status: " + (invoice.isPaid() ? "Paid" : "Unpaid"), normalFont));
        document.add(new Paragraph("State Code: 32", normalFont));
        document.add(new Paragraph("GSTIN: " + invoice.getCustomer().getGstin(), normalFont));
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 3f, 2f, 2f, 2f});
        addTableHeader(table, "SL No", "Product", "Quantity", "Price", "Amount");

        int serialNo = 1;
        double cgstAmount = 0.0;
        double sgstAmount = 0.0;
        double igstAmount = 0.0;

        for (InvoiceItem item : invoice.getItems()) {
            Product product = item.getProduct();
            double amount = product.getPrice() * item.getQuantity();

            table.addCell(String.valueOf(serialNo++));
            table.addCell(product.getName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.format("%.2f", product.getPrice()));
            table.addCell(String.format("%.2f", amount));

            cgstAmount += safeGST(product.getCgstPercentage(), amount);
            sgstAmount += safeGST(product.getSgstPercentage(), amount);
            igstAmount += safeGST(product.getIgstPercentage(), amount);
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Net Taxable Amount: ₹" + String.format("%.2f", invoice.getTotalAmount()), normalFont));
        document.add(new Paragraph("CGST Amount: ₹" + String.format("%.2f", cgstAmount), normalFont));
        document.add(new Paragraph("SGST Amount: ₹" + String.format("%.2f", sgstAmount), normalFont));
        document.add(new Paragraph("IGST Amount: ₹" + String.format("%.2f", igstAmount), normalFont));

        double totalWithGST = invoice.getTotalAmount() + cgstAmount + sgstAmount + igstAmount;
        document.add(new Paragraph("Total Amount with GST: ₹" + String.format("%.2f", totalWithGST), normalFont));

        document.close();
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(Color.LIGHT_GRAY);
            headerCell.setPhrase(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            table.addCell(headerCell);
        }
    }
}