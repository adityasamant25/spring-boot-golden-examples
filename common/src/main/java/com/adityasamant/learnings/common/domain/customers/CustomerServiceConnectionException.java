package com.adityasamant.learnings.common.domain.customers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class CustomerServiceConnectionException extends RuntimeException {

    public CustomerServiceConnectionException(String message) {
        super(message);
    }
}
