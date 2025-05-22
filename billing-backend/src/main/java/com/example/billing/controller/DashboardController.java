package com.example.billing.controller;

import com.example.billing.dto.ProductSummary;
import com.example.billing.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        dashboardService.getTopSellingProducts(5);
        summary.put("totalRevenue", dashboardService.getTotalRevenue());
        summary.put("invoiceCount", dashboardService.getTotalInvoices());
        summary.put("totalMonthlyRevenue", dashboardService.getMonthlyRevenue());
        summary.put("totalRevenueByCategory", dashboardService.getRevenueByCategory());
        summary.put("customerName", dashboardService.getCustomerName());
        summary.put("recentInvoices", dashboardService.getRecentInvoices());
        return summary;
    }
}