package com.adityasamant.learnings.customersbasic.domain;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerCollectionRepository {

    private final List<Customer> customerList = new ArrayList<>();

    public CustomerCollectionRepository() {}

    public List<Customer> findAll() {
        return customerList;
    }

    public Optional<Customer> findById(Integer id) {
        return customerList.stream().filter(c -> c.id().equals(id)).findFirst();
    }

    public void save(Customer customer) {
        customerList.removeIf(c -> c.id().equals(customer.id()));
        customerList.add(customer);
    }

    public boolean checkInvalidCustomer(Integer id) {
        return customerList.stream().filter(c -> c.id().equals(id)).count() != 1;
    }

    public void delete(Integer id) {
        customerList.removeIf(c -> c.id().equals(id));
    }

    @PostConstruct
    private void init() {
        Customer c1 = new Customer(1, "John", "Doe", "Australia");
        Customer c2 = new Customer(2, "Alice", "Smith", "USA");
        Customer c3 = new Customer(3, "Bob", "Stevens", "England");
        customerList.add(c1);
        customerList.add(c2);
        customerList.add(c3);
    }
}
