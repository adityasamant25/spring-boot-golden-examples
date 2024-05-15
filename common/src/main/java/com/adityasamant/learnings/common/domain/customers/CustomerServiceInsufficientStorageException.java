package com.adityasamant.learnings.common.domain.customers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
public class CustomerServiceInsufficientStorageException extends RuntimeException {
    public CustomerServiceInsufficientStorageException(String message) {
        super(message);
    }
}
