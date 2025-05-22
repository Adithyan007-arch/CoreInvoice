package com.example.billing.controller;

import com.example.billing.model.Product;
import com.example.billing.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.billing.model.Product;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductRepository productRepository;

    // ✅ Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");
        return productRepository.findAll();
    }

    // ✅ Get one product by ID (for edit)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        logger.debug("Product: " + product.getName() + " fetched Successfully");
        // Check if the product exists
        if (!productRepository.existsById(id)) {
            logger.warn("Product with ID " + id + " not found");
            return ResponseEntity.notFound().build();
        }
        return productRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        logger.info("Creating new product: " + product.getName() + " with price: " +  product.getPrice());
        return productRepository.save(product);
    }

    // ✅ Update product by ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        logger.info("Updating product with ID: " + id);
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setCategory(updatedProduct.getCategory());
            product.setSgstPercentage(updatedProduct.getSgstPercentage());
            product.setCgstPercentage(updatedProduct.getCgstPercentage());
            product.setIgstPercentage(updatedProduct.getIgstPercentage());
            // Calculate the final amount based on the SGST and CGST and IGST percentages
            product.setFinalAmount(updatedProduct.getFinalAmount());
            logger.debug("Product updated: " + product.getName() + " with new price: " + product.getPrice());
            return ResponseEntity.ok(productRepository.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }

    // (Optional) Delete product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("About to delete Product:  " + productRepository.findById(id).get().getName());
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            logger.info("Deleted the product:  " + product.getName());
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}