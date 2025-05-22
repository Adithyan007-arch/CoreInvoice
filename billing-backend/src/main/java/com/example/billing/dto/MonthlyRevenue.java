package com.example.billing.dto;

public class MonthlyRevenue {
    private int month;
    private double revenue;

    public MonthlyRevenue(int month, double revenue) {
        this.month = month;
        this.revenue = revenue;
    }

    public int getMonth() {
        return month;
    }

    public double getRevenue() {
        return revenue;
    }
}