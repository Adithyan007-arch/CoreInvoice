package com.example.billing.service;
import com.example.billing.dto.CategoryRevenue;
import com.example.billing.dto.MonthlyRevenue;
import com.example.billing.model.Customer;
import com.example.billing.model.Invoice;
import com.example.billing.model.Product;
import com.example.billing.repository.CustomerRepository;
import com.example.billing.repository.InvoiceRepository;
import com.example.billing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DashboardService(InvoiceRepository invoiceRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public double getTotalRevenue() {
        logger.debug("Calculating total revenue from all invoices.");
        double total= invoiceRepository.findAll()
        .stream()
        .mapToDouble(Invoice::getTotalAmount)
        .sum();
        logger.debug("Total revenue: {}", total);
        logger.debug("Fetched the Total Revenue Successfully.");
        return total;
    }

    public long getTotalInvoices() {
        logger.debug("Counting total number of invoices.");
        logger.debug("Total invoices: {}", invoiceRepository.count());
        return invoiceRepository.count();
    }

    public String getCustomerName()
    {
        logger.debug("Fetching all customer names.");
        logger.debug("Customer names: {}", customerRepository.findAll()
                .stream()
                .map(Customer::getName)
                .collect(Collectors.joining(", ")));
        return customerRepository.findAll()
                .stream()
                .map(Customer::getName)
                .collect(Collectors.joining(", "));
    }

    public List<Object[]> getRecentInvoices() {
        logger.debug("Fetching recent invoices.");
        return invoiceRepository.findRecentInvoices();
    }

    public List<MonthlyRevenue> getMonthlyRevenue()
    {
        List<Object[]> rows = invoiceRepository.findMonthlyRevenue();
    List<MonthlyRevenue> revenues = new ArrayList<>();
    for (Object[] row : rows) {
        int month = ((Number) row[0]).intValue();
        double revenue = ((Number) row[1]).doubleValue();
        revenues.add(new MonthlyRevenue(month, revenue));
        logger.debug("Month: {}, Revenue: {}", month, revenue);
        logger.debug("Added the Revenue Successfully.");
    }
    return revenues;
    }

    public List<CategoryRevenue> getRevenueByCategory()
    {
        logger.debug("Fetching revenue by category.");
        logger.debug("Revenue by category: {}", invoiceRepository.findRevenueByCategory());
        logger.debug("Fetched the Revenue by Category Successfully.");
        return invoiceRepository.findRevenueByCategory();
    }
    public List<Product> getTopSellingProducts(int limit) {
        logger.debug("Fetching the top selling products .....");
        return invoiceRepository.findAll().stream()
                .flatMap(invoice -> invoice.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct(),
                        Collectors.summingInt(item -> item.getQuantity())
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
