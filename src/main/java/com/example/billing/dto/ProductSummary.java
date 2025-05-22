package com.example.billing.dto;

public class ProductSummary {
    private String name;
    private Long totalQuantity;
    private Double totalRevenue;
    private long totalSold;

    public ProductSummary(String name, Long totalQuantity, Double totalRevenue, Long totalSold) {
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
        this.totalSold = totalSold;
    }
    public Long getTotalSold() {
        return totalSold;
    }
    public void setTotalSold(Long totalSold) {
        this.totalSold = totalSold;
    }

    public String getName() {
        return name;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }
}