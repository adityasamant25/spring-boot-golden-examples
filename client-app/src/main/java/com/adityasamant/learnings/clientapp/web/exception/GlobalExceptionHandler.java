package com.adityasamant.learnings.clientapp.web.exception;

import com.adityasamant.learnings.common.domain.customers.CustomerNotFoundException;
import com.adityasamant.learnings.common.domain.customers.CustomerServiceAuthorizationException;
import com.adityasamant.learnings.common.domain.customers.CustomerServiceConnectionException;
import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final URI NOT_FOUND_TYPE = URI.create("https://api.client-app.com/errors/not-found");
    private static final URI ISE_FOUND_TYPE = URI.create("https://api.client-app.com/errors/server-error");
    private static final URI SECURITY_TYPE = URI.create("https://api.client-app.com/errors/security-error");
    private static final URI CONNECTIVITY_TYPE = URI.create("https://api.client-app.com/errors/connectivity-error");
    private static final String SERVICE_NAME = "client-app";

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnhandledException(Exception e) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(ISE_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(CustomerServiceConnectionException.class)
    ProblemDetail handleCustomerServiceConnectionException(CustomerServiceConnectionException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
        problemDetail.setTitle("Connection to Customer Service Failed");
        problemDetail.setType(CONNECTIVITY_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(CustomerServiceAuthorizationException.class)
    ProblemDetail handleCustomerServiceAuthorizationException(CustomerServiceAuthorizationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setTitle("Authorization to Customer Service Failed");
        problemDetail.setType(SECURITY_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Customer Not Found");
        problemDetail.setType(NOT_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
