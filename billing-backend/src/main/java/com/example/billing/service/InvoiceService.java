package com.example.billing.service;

import com.example.billing.dto.InvoiceRequest;
import com.example.billing.model.Customer;
import com.example.billing.model.Invoice;
import com.example.billing.model.InvoiceItem;
import com.example.billing.model.Product;
import com.example.billing.repository.CustomerRepository;
import com.example.billing.repository.InvoiceItemRepository;
import com.example.billing.repository.InvoiceRepository;
import com.example.billing.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    public Invoice createInvoice(InvoiceRequest request) {
        Optional<Customer> optionalCustomer = customerRepository.findById(request.getCustomerId());
        if (!optionalCustomer.isPresent()) throw new RuntimeException("Customer not found");

        Invoice invoice = new Invoice();
        invoice.setCustomer(optionalCustomer.get());
        invoice.setDate(LocalDate.now());
        invoice.setPaid(request.isPaid());

        invoice = invoiceRepository.save(invoice);

        List<InvoiceItem> items = new ArrayList<>();
        double total = 0;

        for (InvoiceRequest.Item item : request.getItems()) {
            Optional<Product> optionalProduct = productRepository.findById(item.getProductId());
            if (!optionalProduct.isPresent()) continue;

            Product product = optionalProduct.get();
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProduct(product);
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setPrice(product.getPrice());
            invoiceItem.setInvoice(invoice);
            items.add(invoiceItem);
            total += product.getPrice() * item.getQuantity();
        }

        invoice.setTotalAmount(total);

        invoiceItemRepository.saveAll(items);

        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }
}