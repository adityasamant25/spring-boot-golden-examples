package com.adityasamant.learnings.customersbasic.domain;

import domain.customers.CustomerDTO;

public class CustomerMapper {

    public static Customer toCustomer(CustomerDTO customerDTO) {
        return new Customer(customerDTO.id(), customerDTO.firstName(), customerDTO.lastName(), customerDTO.country());
    }

    public static CustomerDTO toCustomerDTO(Customer customer) {
        return new CustomerDTO(customer.id(), customer.firstName(), customer.lastName(), customer.country());
    }
}
