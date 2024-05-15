package com.adityasamant.learnings.customersbasic.web.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.adityasamant.learnings.common.domain.customers.CustomerDTO;
import com.adityasamant.learnings.common.domain.customers.CustomerNotFoundException;
import com.adityasamant.learnings.common.domain.customers.CustomerServiceInsufficientStorageException;
import com.adityasamant.learnings.customersbasic.domain.Customer;
import com.adityasamant.learnings.customersbasic.domain.CustomerCollectionRepository;
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
        assertThrows(CustomerServiceInsufficientStorageException.class, () -> controller.findAll(""));
    }

    @Test
    void findAllWithNullUserHeader() {
        assertThrows(CustomerServiceInsufficientStorageException.class, () -> controller.findAll(null));
    }

    @Test
    void findAllWithUserHeader() {
        assertThrows(CustomerServiceInsufficientStorageException.class, () -> controller.findAll("debug"));
    }

    @Test
    void findById() {
        CustomerDTO customer = controller.findById(1);
        assertEquals(1, customer.id());
        assertEquals("John", customer.firstName());
        assertEquals("Doe", customer.lastName());
    }

    @Test
    void create() {
        controller.create(new CustomerDTO(4, "Sandra", "Peters", "India"));
        CustomerDTO customers = controller.findById(4);
        assertEquals("Sandra", customers.firstName());
    }

    @Test
    void update() {
        controller.update(new CustomerDTO(1, "John", "Gray", "New Zealand"), 1);
        CustomerDTO customer = controller.findById(1);
        assertEquals("Gray", customer.lastName());
    }

    @Test
    void delete() {
        controller.delete(3);
        assertThrows(CustomerNotFoundException.class, () -> controller.findById(3));
    }
}
