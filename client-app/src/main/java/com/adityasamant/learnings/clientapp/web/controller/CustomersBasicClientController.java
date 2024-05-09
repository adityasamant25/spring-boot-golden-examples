package com.adityasamant.learnings.clientapp.web.controller;

import com.adityasamant.learnings.clientapp.domain.client.CustomersBasicClient;
import com.adityasamant.learnings.common.domain.customers.CustomerDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomersBasicClientController {

    private final CustomersBasicClient customersBasicClient;

    public CustomersBasicClientController(CustomersBasicClient customersBasicClient) {
        this.customersBasicClient = customersBasicClient;
    }

    @GetMapping("")
    public List<CustomerDTO> findAllCustomers(@RequestHeader(name = "user", required = false) String user) {
        return customersBasicClient.findAllCustomers(user);
    }

    @GetMapping("/{id}")
    public CustomerDTO findCustomerById(@PathVariable Integer id) {
        return customersBasicClient.findCustomerById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void create(@RequestBody @Valid CustomerDTO customerDTO) {
        customersBasicClient.createCustomer(customerDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateCustomer(@RequestBody @Valid CustomerDTO customerDTO, @PathVariable Integer id) {
        customersBasicClient.updateCustomer(customerDTO, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        customersBasicClient.deleteCustomer(id);
    }
}
