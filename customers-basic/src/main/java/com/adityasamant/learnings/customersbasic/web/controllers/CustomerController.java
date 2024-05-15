package com.adityasamant.learnings.customersbasic.web.controllers;

import com.adityasamant.learnings.common.domain.customers.CustomerDTO;
import com.adityasamant.learnings.common.domain.customers.CustomerNotFoundException;
import com.adityasamant.learnings.common.domain.customers.CustomerServiceInsufficientStorageException;
import com.adityasamant.learnings.customersbasic.domain.CustomerCollectionRepository;
import com.adityasamant.learnings.customersbasic.domain.CustomerMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<CustomerDTO> findAll(@RequestHeader(value = "user", required = false) String user) {
        log.info("findAll is called with user header: {}", user);
        throw new CustomerServiceInsufficientStorageException("Request failed due to insufficient storage");
    }

    @GetMapping("/{id}")
    public CustomerDTO findById(@PathVariable Integer id) {
        return CustomerMapper.toCustomerDTO(repository.findById(id).orElseThrow(CustomerNotFoundException::new));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void create(@RequestBody @Valid CustomerDTO customerDTO) {
        repository.save(CustomerMapper.toCustomer(customerDTO));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@RequestBody @Valid CustomerDTO customerDTO, @PathVariable Integer id) {
        if (repository.checkInvalidCustomer(id)) {
            throw new CustomerNotFoundException("Customer not found.");
        }
        repository.save(CustomerMapper.toCustomer(customerDTO));
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
