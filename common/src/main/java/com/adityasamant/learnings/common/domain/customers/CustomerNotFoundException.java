package com.adityasamant.learnings.common.domain.customers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {}

    public CustomerNotFoundException(String s) {
        super(s);
    }
}
