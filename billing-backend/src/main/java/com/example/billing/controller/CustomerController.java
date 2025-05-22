package com.example.billing.controller;

import com.example.billing.model.Customer;
import com.example.billing.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.billing.model.Customer;
import com.example.billing.repository.CustomerRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer updated) {
        return customerRepository.findById(id).map(c -> {
            c.setName(updated.getName());
            c.setEmail(updated.getEmail());
            return ResponseEntity.ok(customerRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
