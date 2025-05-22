package com.example.billing.repository;

import com.example.billing.model.InvoiceItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    @Query("SELECT i.product.name, SUM(i.quantity) FROM InvoiceItem i GROUP BY i.product.name ORDER BY SUM(i.quantity) DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);
}