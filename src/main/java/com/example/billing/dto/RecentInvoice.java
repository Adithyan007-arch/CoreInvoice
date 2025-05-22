package com.example.billing.dto;

public class RecentInvoice {
    private Long invoiceId;
    private String customerName;
    private double amount;
    private String status;

    public RecentInvoice(Long invoiceId, String customerName, double amount, String status) {
        this.invoiceId = invoiceId;
        this.customerName = customerName;
        this.amount = amount;
        this.status = status;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}