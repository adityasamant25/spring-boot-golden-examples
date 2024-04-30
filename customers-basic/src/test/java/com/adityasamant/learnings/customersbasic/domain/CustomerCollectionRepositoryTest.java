package com.adityasamant.learnings.customersbasic.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class CustomerCollectionRepositoryTest {

    private final CustomerCollectionRepository repository = new CustomerCollectionRepository();
    Customer customer = null;

    @BeforeEach
    void setUp() {
        // Given : A customer
        customer = new Customer(1, "John", "Doe", "Australia");
    }


    @Test
    void saveAndFindAll() {
        // When
        repository.save(customer);

        // Then
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findById() {
        // When
        repository.save(customer);

        // Then
        assertEquals(Optional.of(customer), repository.findById(1));
    }


    @Test
    void checkInvalidCustomer() {
        // When
        repository.save(customer);

        // Then
        assertTrue(repository.checkInvalidCustomer(2));
        assertFalse(repository.checkInvalidCustomer(1));
    }

    @Test
    void delete() {
        // When
        repository.save(customer);
        repository.delete(1);
        // Then
        assertEquals(0, repository.findAll().size());
    }
}