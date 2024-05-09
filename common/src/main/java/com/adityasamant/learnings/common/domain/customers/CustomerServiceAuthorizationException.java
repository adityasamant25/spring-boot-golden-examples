package com.adityasamant.learnings.common.domain.customers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomerServiceAuthorizationException extends RuntimeException {

    public CustomerServiceAuthorizationException(String message) {
        super(message);
    }
}
