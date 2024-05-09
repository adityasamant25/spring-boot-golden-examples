package com.adityasamant.learnings.clientapp.domain.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.adityasamant.learnings.common.domain.customers.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.SocketException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@RestClientTest(CustomersBasicClient.class)
public class CustomersBasicClientTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    CustomersBasicClient customersBasicClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldFindAllCustomers() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        // when
        server.expect(requestTo("http://localhost:8081/api/customers"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(customers), MediaType.APPLICATION_JSON));

        // then
        List<CustomerDTO> customerList = customersBasicClient.findAllCustomers(null);
        assertEquals(3, customerList.size());
    }

    @Test
    void shouldFindAllCustomersWithHeader() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        // when
        server.expect(requestTo("http://localhost:8081/api/customers"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(customers), MediaType.APPLICATION_JSON));

        // then
        List<CustomerDTO> customerList = customersBasicClient.findAllCustomers("debug");
        assertEquals(3, customerList.size());
    }

    @Test
    void shouldHandleResourceAccessExceptionInFindAllCustomers() {
        // when
        server.expect(requestTo("http://localhost:8081/api/customers")).andRespond((response) -> {
            throw new ResourceAccessException("Connection reset", new SocketException());
        });

        // then
        assertThrows(CustomerServiceConnectionException.class, () -> customersBasicClient.findAllCustomers(null));
    }

    @Test
    void shouldHandleResourceAccessExceptionInFindAllCustomersWithHeader() {
        // when
        server.expect(requestTo("http://localhost:8081/api/customers")).andRespond((response) -> {
            throw new ResourceAccessException("Connection reset", new SocketException());
        });

        // then
        assertThrows(CustomerServiceConnectionException.class, () -> customersBasicClient.findAllCustomers("debug"));
    }

    @Test
    void shouldHandleHttpClientErrorExceptionInFindAllCustomers() {
        // when
        server.expect(requestTo("http://localhost:8081/api/customers")).andRespond((response) -> {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "RBAC: access denied");
        });

        // then
        assertThrows(CustomerServiceAuthorizationException.class, () -> customersBasicClient.findAllCustomers(null));
    }

    @Test
    void shouldHandleHttpClientErrorExceptionInFindAllCustomersWithHeader() {
        // when
        server.expect(requestTo("http://localhost:8081/api/customers")).andRespond((response) -> {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "RBAC: access denied");
        });

        // then
        assertThrows(CustomerServiceAuthorizationException.class, () -> customersBasicClient.findAllCustomers("debug"));
    }

    @Test
    void shouldFindCustomerByValidId() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        // when
        server.expect(requestTo("http://localhost:8081/api/customers/2"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(customers.get(1)), MediaType.APPLICATION_JSON));

        // then
        CustomerDTO customerDTO = customersBasicClient.findCustomerById(2);
        assertEquals(2, customerDTO.id());
    }

    @Test
    void shouldHandleResourceAccessExceptionInFindCustomerById() {
        // when
        server.expect(requestTo("http://localhost:8081/api/customers/2")).andRespond((response) -> {
            throw new ResourceAccessException("Connection reset", new SocketException());
        });

        // then
        assertThrows(CustomerServiceConnectionException.class, () -> customersBasicClient.findCustomerById(2));
    }

    @Test
    void shouldHandleHttpClientErrorExceptionInFindCustomerById() {
        // when
        server.expect(requestTo("http://localhost:8081/api/customers/2")).andRespond((response) -> {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "RBAC: access denied");
        });

        // then
        assertThrows(CustomerServiceAuthorizationException.class, () -> customersBasicClient.findCustomerById(2));
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionForInvalidCustomerId() {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        // when
        server.expect(requestTo("http://localhost:8081/api/customers/4")).andRespond((response) -> {
            throw new CustomerNotFoundException();
        });

        // then
        assertThrows(CustomerNotFoundException.class, () -> customersBasicClient.findCustomerById(4));
    }

    @Test
    void shouldCreateNewCustomer() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        CustomerDTO newCustomer = new CustomerDTO(4, "Bhavana", "Pandey", "India");

        // when
        server.expect(requestTo("http://localhost:8081/api/customers"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(objectMapper.writeValueAsString(newCustomer)))
                .andRespond(withStatus(HttpStatus.CREATED));

        // then
        customersBasicClient.createCustomer(newCustomer);

        server.verify();
    }

    @Test
    void shouldThrowBadRequestExceptionForInvalidCustomerCreation() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        CustomerDTO newCustomer = new CustomerDTO(4, "", "Pandey", "India");

        // when
        server.expect(requestTo("http://localhost:8081/api/customers"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(objectMapper.writeValueAsString(newCustomer)))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // then
        assertThrows(CustomerServiceBadRequestException.class, () -> customersBasicClient.createCustomer(newCustomer));
        // customersBasicClient.createCustomer(newCustomer);

        server.verify();
    }

    @Test
    void shouldUpdateExistingCustomer() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        CustomerDTO newCustomer = new CustomerDTO(1, "NewFirstName", "NewLastName", "Spain");

        // when
        server.expect(requestTo("http://localhost:8081/api/customers/1"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(objectMapper.writeValueAsString(newCustomer)))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        // then
        customersBasicClient.updateCustomer(newCustomer, 1);

        server.verify();
    }

    @Test
    void shouldFailUpdatingExistingCustomerDuetoBadRequest() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        CustomerDTO newCustomer = new CustomerDTO(1, "NewFirstName", "", "Spain");

        // when
        server.expect(requestTo("http://localhost:8081/api/customers/1"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(objectMapper.writeValueAsString(newCustomer)))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // then
        assertThrows(
                CustomerServiceBadRequestException.class, () -> customersBasicClient.updateCustomer(newCustomer, 1));
        // customersBasicClient.updateCustomer(newCustomer, 1);

        server.verify();
    }

    @Test
    void shouldFailUpdatingExistingCustomerDuetoNonExistingCustomer() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        CustomerDTO newCustomer = new CustomerDTO(98, "NewFirstName", "NewLastName", "Spain");

        // when
        server.expect(requestTo("http://localhost:8081/api/customers/98"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(objectMapper.writeValueAsString(newCustomer)))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // then
        assertThrows(CustomerNotFoundException.class, () -> customersBasicClient.updateCustomer(newCustomer, 98));
        // customersBasicClient.updateCustomer(newCustomer, 98);

        server.verify();
    }

    @Test
    void shouldDeleteExistingCustomer() throws JsonProcessingException {
        // given
        List<CustomerDTO> customers = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));

        // when
        server.expect(requestTo("http://localhost:8081/api/customers/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        // then
        customersBasicClient.deleteCustomer(1);

        server.verify();
    }
}
