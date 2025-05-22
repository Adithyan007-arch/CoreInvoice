package com.example.billing.dto;

import com.example.billing.model.Customer;
import com.example.billing.model.InvoiceItem;

import java.time.LocalDate;
import java.util.List;

public class InvoiceResponse {
    private Long id;
    private Customer customer;
    private List<InvoiceItem> items;
    private LocalDate date;
    private boolean paid;
    private double totalAmount;

    public InvoiceResponse() {}

    public InvoiceResponse(Long id, Customer customer, List<InvoiceItem> items, LocalDate date, boolean paid, double totalAmount) {
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.date = date;
        this.paid = paid;
        this.totalAmount = totalAmount;
    }

    public InvoiceResponse(Long id2, Customer customer2, List<InvoiceItem> items2, LocalDate date2, String paid2,
            double totalAmount2) {
        //TODO Auto-generated constructor stub
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}