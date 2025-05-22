package com.example.billing.service;

import com.example.billing.model.Product;
import com.example.billing.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
    public void gstCalculator(Product product) {
        double gstPercentage = Double.parseDouble(product.getGstpercentage());
        double price = product.getPrice();
        double gstAmount = (price * gstPercentage) / 100;
        double finalAmount = price + gstAmount;
        product.setFinal_amount(finalAmount);
        productRepository.save(product);
    }
}
