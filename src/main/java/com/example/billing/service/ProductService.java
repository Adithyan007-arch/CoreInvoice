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
        double sgst = Double.parseDouble(product.getSgstPercentage());
        double cgst = Double.parseDouble(product.getCgstPercentage());
        double igst = Double.parseDouble(product.getIgstPercentage());
        // Calculate the final amount based on the SGST and CGST and IGST percentages
        double finalAmount = product.getPrice() + (product.getPrice() * sgst / 100) + (product.getPrice() * cgst / 100 + (product.getPrice() * igst / 100));
        product.setFinalAmount(finalAmount);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
