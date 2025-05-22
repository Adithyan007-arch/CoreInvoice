package com.example.billing.dto;

import java.util.List;

public class DashboardResponse {
    private double totalRevenue;
    private long totalInvoices;
    private long totalCustomers;
    private List<MonthlyRevenue> monthlyRevenue;
    private List<CategoryRevenue> categoryRevenue;
    private List<RecentInvoice> recentInvoices;

    public DashboardResponse(double totalRevenue, long totalInvoices, long totalCustomers,
                             List<MonthlyRevenue> monthlyRevenue,
                             List<CategoryRevenue> categoryRevenue,
                             List<RecentInvoice> recentInvoices) {
        this.totalRevenue = totalRevenue;
        this.totalInvoices = totalInvoices;
        this.totalCustomers = totalCustomers;
        this.monthlyRevenue = monthlyRevenue;
        this.categoryRevenue = categoryRevenue;
        this.recentInvoices = recentInvoices;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public long getTotalInvoices() {
        return totalInvoices;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public List<MonthlyRevenue> getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public List<CategoryRevenue> getCategoryRevenue() {
        return categoryRevenue;
    }

    public List<RecentInvoice> getRecentInvoices() {
        return recentInvoices;
    }
}