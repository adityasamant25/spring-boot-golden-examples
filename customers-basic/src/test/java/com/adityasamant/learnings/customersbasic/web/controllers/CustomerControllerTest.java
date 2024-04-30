package com.adityasamant.learnings.customersbasic.web.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adityasamant.learnings.customersbasic.domain.Customer;
import com.adityasamant.learnings.customersbasic.domain.CustomerCollectionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerControllerTest {

    CustomerCollectionRepository customerCollectionRepository = null;
    CustomerController controller = null; // Arrange

    @BeforeEach
    void setUp() {
        customerCollectionRepository = new CustomerCollectionRepository();
        controller = new CustomerController(customerCollectionRepository);
        customerCollectionRepository.save(new Customer(1, "John", "Doe", "Australia"));
        customerCollectionRepository.save(new Customer(2, "Alice", "Smith", "USA"));
        customerCollectionRepository.save(new Customer(3, "Bob", "Stevens", "England"));
    }

    @Test
    void findAllWithBlankUserHeader() {
        List<Customer> customers = controller.findAll(""); // Act
        assertEquals(3, customers.size()); // Assert
    }

    @Test
    void findAllWithNullUserHeader() {
        List<Customer> customers = controller.findAll(null); // Act
        assertEquals(3, customers.size()); // Assert
    }

    @Test
    void findAllWithUserHeader() {
        List<Customer> customers = controller.findAll("debug"); // Act
        assertEquals(3, customers.size()); // Assert
    }

    @Test
    void findById() {
        Customer customer = controller.findById(1);
        assertEquals(1, customer.id());
        assertEquals("John", customer.firstName());
        assertEquals("Doe", customer.lastName());
    }

    @Test
    void create() {
        controller.create(new Customer(4, "Sandra", "Peters", "India"));
        List<Customer> customers = controller.findAll("");
        assertEquals(4, customers.size());
    }

    @Test
    void update() {
        controller.update(new Customer(1, "John", "Gray", "New Zealand"), 1);
        Customer customer = controller.findById(1);
        assertEquals("Gray", customer.lastName());
    }

    @Test
    void delete() {
        controller.delete(3);
        List<Customer> customers = controller.findAll("");
        assertEquals(2, customers.size());
    }
}
