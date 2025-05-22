package com.example.billing.service;
import com.example.billing.model.Customer;
import com.example.billing.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        if (customer.getId() != null && !customerRepository.existsById(customer.getId())) {
            throw new RuntimeException("Customer not found with id: " + customer.getId());
        }
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new RuntimeException("Customer name cannot be null or empty");
        }
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}