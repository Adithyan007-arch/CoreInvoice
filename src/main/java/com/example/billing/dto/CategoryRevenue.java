package com.example.billing.dto;

public class CategoryRevenue {
    private String category;
    private double total;

    public CategoryRevenue(String category, double total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public double getTotal() {
        return total;
    }
}