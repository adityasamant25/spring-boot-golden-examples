package com.adityasamant.learnings.customersbasic.web.controllers;

import com.adityasamant.learnings.customersbasic.domain.Customer;
import com.adityasamant.learnings.customersbasic.domain.CustomerCollectionRepository;
import com.adityasamant.learnings.customersbasic.domain.CustomerNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {


    Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerCollectionRepository repository;

    public CustomerController(CustomerCollectionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public List<Customer> findAll(@RequestHeader(value = "user", required = false) String user) {
        log.info("findAll is called with user header: {}", user);
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(CustomerNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void create(@RequestBody @Valid Customer customer) {
        repository.save(customer);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@RequestBody @Valid Customer customer, @PathVariable Integer id) {
        if (repository.checkInvalidCustomer(id)) {
            throw new CustomerNotFoundException("Customer not found.");
        }
        repository.save(customer);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        if (repository.checkInvalidCustomer(id)) {
            throw new CustomerNotFoundException("Customer not found.");
        }
        repository.delete(id);
    }

}
