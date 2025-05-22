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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAll() {
        logger.debug("Checking connectivity to the database");
        logger.debug("Going forward to fetch all customers from the database");
        return customerRepository.findAll();
    }

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        logger.info("Adding new customer: " + customer.getName());
        if (customer.getName() == null || customer.getEmail() == null) {
            throw new IllegalArgumentException("Customer name and email cannot be null");
        }
        if (customer.getName().isEmpty() || customer.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Customer name and email cannot be empty");
        }
        if (customer.getEmail().length() > 50) {
            throw new IllegalArgumentException("Customer email cannot exceed 50 characters");
        }
        if (customer.getName().length() > 50) {
            throw new IllegalArgumentException("Customer name cannot exceed 50 characters");
        }
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
            c.setPhone(updated.getPhone());
            c.setGstin(updated.getGstin());
            logger.info("Updating customer with Name: " + c.getName());
            return ResponseEntity.ok(customerRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
                logger.info("Deleting Customer : " + customer.getName() + " from database position: " + id);
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
