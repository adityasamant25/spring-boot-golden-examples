package com.adityasamant.learnings.clientapp.domain.client;

import com.adityasamant.learnings.common.domain.customers.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class CustomersBasicClient {

    private final RestClient restClient;

    public CustomersBasicClient(
            RestClient.Builder builder, @Value("${customers.basic.service.url}") String customerServiceUrl) {
        this.restClient = builder.baseUrl(customerServiceUrl).build();
    }

    public List<CustomerDTO> findAllCustomers(String user) {
        try {
            if (user == null) {
                user = "";
            }

            return restClient
                    .get()
                    .uri("/api/customers")
                    .header("user", user)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (ResourceAccessException resourceAccessException) {
            throw new CustomerServiceConnectionException(resourceAccessException.getMessage());
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new CustomerServiceAuthorizationException(httpClientErrorException.getMessage());
        }
    }

    public CustomerDTO findCustomerById(Integer id) {
        try {
            return restClient
                    .get()
                    .uri("/api/customers/{id}", id)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (ResourceAccessException resourceAccessException) {
            throw new CustomerServiceConnectionException(resourceAccessException.getMessage());
        } catch (HttpClientErrorException httpClientErrorException) {
            if (HttpStatus.NOT_FOUND.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerNotFoundException(httpClientErrorException.getMessage());
            } else if (HttpStatus.FORBIDDEN.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerServiceAuthorizationException(httpClientErrorException.getMessage());
            } else {
                throw new RuntimeException(httpClientErrorException.getMessage());
            }
        }
    }

    public void createCustomer(CustomerDTO customerDTO) {
        try {
            restClient
                    .post()
                    .uri("/api/customers")
                    .body(customerDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (ResourceAccessException resourceAccessException) {
            throw new CustomerServiceConnectionException(resourceAccessException.getMessage());
        } catch (HttpClientErrorException httpClientErrorException) {
            if (HttpStatus.NOT_FOUND.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerNotFoundException(httpClientErrorException.getMessage());
            } else if (HttpStatus.FORBIDDEN.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerServiceAuthorizationException(httpClientErrorException.getMessage());
            } else if (HttpStatus.BAD_REQUEST.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerServiceBadRequestException(httpClientErrorException.getMessage());
            } else {
                throw new RuntimeException(httpClientErrorException.getMessage());
            }
        }
    }

    public void updateCustomer(CustomerDTO customerDTO, Integer id) {
        try {
            restClient
                    .put()
                    .uri("/api/customers/{id}", id)
                    .body(customerDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (ResourceAccessException resourceAccessException) {
            throw new CustomerServiceConnectionException(resourceAccessException.getMessage());
        } catch (HttpClientErrorException httpClientErrorException) {
            if (HttpStatus.NOT_FOUND.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerNotFoundException(httpClientErrorException.getMessage());
            } else if (HttpStatus.FORBIDDEN.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerServiceAuthorizationException(httpClientErrorException.getMessage());
            } else if (HttpStatus.BAD_REQUEST.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerServiceBadRequestException(httpClientErrorException.getMessage());
            } else {
                throw new RuntimeException(httpClientErrorException.getMessage());
            }
        }
    }

    public void deleteCustomer(Integer id) {
        try {
            restClient.delete().uri("/api/customers/{id}", id).retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (ResourceAccessException resourceAccessException) {
            throw new CustomerServiceConnectionException(resourceAccessException.getMessage());
        } catch (HttpClientErrorException httpClientErrorException) {
            if (HttpStatus.NOT_FOUND.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerNotFoundException(httpClientErrorException.getMessage());
            } else if (HttpStatus.FORBIDDEN.equals(httpClientErrorException.getStatusCode())) {
                throw new CustomerServiceAuthorizationException(httpClientErrorException.getMessage());
            } else {
                throw new RuntimeException(httpClientErrorException.getMessage());
            }
        }
    }
}
