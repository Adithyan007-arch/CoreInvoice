package com.example.billing.repository;

import com.example.billing.dto.MonthlyRevenue;
import com.example.billing.dto.CategoryRevenue;
import com.example.billing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT EXTRACT(MONTH FROM i.date) AS month, SUM(i.total_amount) AS revenue FROM invoice i GROUP BY EXTRACT(MONTH FROM i.date) ORDER BY month", nativeQuery = true)
    List<Object[]> findMonthlyRevenue();

    @Query("SELECT NEW com.example.billing.dto.CategoryRevenue(p.category, SUM(ii.quantity * ii.price)) " +
           "FROM InvoiceItem ii JOIN ii.product p GROUP BY p.category")
    List<CategoryRevenue> findRevenueByCategory();

    @Query("SELECT i.id, c.name, i.totalAmount, i.paid " +
           "FROM Invoice i JOIN i.customer c ORDER BY i.date DESC limit 5")
    List<Object[]> findRecentInvoices();
}