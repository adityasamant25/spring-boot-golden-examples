package com.adityasamant.learnings.common.domain.customers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerServiceBadRequestException extends RuntimeException {
    public CustomerServiceBadRequestException(String message) {
        super(message);
    }
}
