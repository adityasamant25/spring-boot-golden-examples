package com.adityasamant.learnings.clientapp.web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adityasamant.learnings.clientapp.domain.client.CustomersBasicClient;
import com.adityasamant.learnings.common.domain.customers.CustomerDTO;
import com.adityasamant.learnings.common.domain.customers.CustomerNotFoundException;
import com.adityasamant.learnings.common.domain.customers.CustomerServiceAuthorizationException;
import com.adityasamant.learnings.common.domain.customers.CustomerServiceConnectionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomersBasicClientController.class)
public class CustomersBasicClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CustomersBasicClient client;

    List<CustomerDTO> customersAsList = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        customersAsList = List.of(
                new CustomerDTO(1, "John", "Doe", "Australia"),
                new CustomerDTO(2, "Alice", "Smith", "USA"),
                new CustomerDTO(3, "Bob", "Stevens", "England"));
    }

    @Test
    void shouldFindAllCustomers() throws Exception {
        // when
        when(client.findAllCustomers(null)).thenReturn(customersAsList);

        // then
        mvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(customersAsList)));
    }

    @Test
    void shouldFindAllCustomersWithHeader() throws Exception {
        // when
        when(client.findAllCustomers("debug")).thenReturn(customersAsList);

        // then
        mvc.perform(get("/api/customers").header("user", "debug"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(customersAsList)));
    }

    @Test
    void shouldThrowExceptionWhenCustomerServiceDeniesConnection() throws Exception {
        // when
        when(client.findAllCustomers(null)).thenThrow(CustomerServiceConnectionException.class);

        // then
        mvc.perform(get("/api/customers")).andExpect(status().isBadGateway());
    }

    @Test
    void shouldThrowExceptionWhenCustomerServiceDeniesConnectionWithHeader() throws Exception {
        // when
        when(client.findAllCustomers("debug")).thenThrow(CustomerServiceConnectionException.class);

        // then
        mvc.perform(get("/api/customers").header("user", "debug")).andExpect(status().isBadGateway());
    }

    @Test
    void shouldThrowRBACExceptionWhenCustomerServiceDeniesAuthorization() throws Exception {
        // when
        when(client.findAllCustomers(null)).thenThrow(CustomerServiceAuthorizationException.class);

        // then
        mvc.perform(get("/api/customers")).andExpect(status().isForbidden());
    }

    @Test
    void shouldThrowRBACExceptionWhenCustomerServiceDeniesAuthorizationWithHeader() throws Exception {
        // when
        when(client.findAllCustomers("debug")).thenThrow(CustomerServiceAuthorizationException.class);

        // then
        mvc.perform(get("/api/customers").header("user", "debug")).andExpect(status().isForbidden());
    }

    @Test
    void shouldFindCustomerByValidId() throws Exception {
        // when
        when(client.findCustomerById(1)).thenReturn(customersAsList.getFirst());

        // then
        mvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(customersAsList.getFirst())));
    }

    @Test
    void shouldThrowExceptionWhenCustomerServiceDeniesConnectionForFindCustomerById() throws Exception {
        // when
        when(client.findCustomerById(2)).thenThrow(CustomerServiceConnectionException.class);

        // then
        mvc.perform(get("/api/customers/2")).andExpect(status().isBadGateway());
    }

    @Test
    void shouldThrowRBACExceptionWhenCustomerServiceDeniesAuthorizationForFindCustomerById() throws Exception {
        // when
        when(client.findCustomerById(3)).thenThrow(CustomerServiceAuthorizationException.class);

        // then
        mvc.perform(get("/api/customers/3")).andExpect(status().isForbidden());
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionForFindCustomerByInvalidId() throws Exception {
        // when
        when(client.findCustomerById(99)).thenThrow(CustomerNotFoundException.class);

        // then
        mvc.perform(get("/api/customers/99")).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewValidCustomer() throws Exception {
        // given
        CustomerDTO newCustomer = new CustomerDTO(4, "Gareth", "Bale", "Wales");

        // when
        doNothing().when(client).createCustomer(newCustomer);

        // then
        mvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(newCustomer)))
                .andExpect(status().isCreated());

        verify(client, times(1)).createCustomer(newCustomer);
    }

    @Test
    void shouldFailToCreateAnInvalidCustomer() throws Exception {
        // given
        CustomerDTO newCustomer = new CustomerDTO(4, "", "Bale", "Wales");

        // when
        doNothing().when(client).createCustomer(newCustomer);

        // then
        mvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(newCustomer)))
                .andExpect(status().isBadRequest());

        verify(client, times(0)).createCustomer(newCustomer);
    }

    @Test
    void shouldUpdateValidCustomer() throws Exception {
        // given
        CustomerDTO updated = new CustomerDTO(1, "NewFirstName", "NewLastName", "Spain");

        // when
        doNothing().when(client).updateCustomer(updated, 1);

        // then
        mvc.perform(put("/api/customers/1").contentType("application/json").content(mapper.writeValueAsString(updated)))
                .andExpect(status().isNoContent());

        verify(client, times(1)).updateCustomer(updated, 1);
    }

    @Test
    void shouldFailToUpdateAnInvalidCustomerDueToBadRequest() throws Exception {
        // given
        CustomerDTO updated = new CustomerDTO(1, "NewFirstName", "", "Spain");

        // when
        doNothing().when(client).updateCustomer(updated, 1);

        // then
        mvc.perform(put("/api/customers/1").contentType("application/json").content(mapper.writeValueAsString(updated)))
                .andExpect(status().isBadRequest());

        verify(client, times(0)).updateCustomer(updated, 1);
    }

    @Test
    void shouldFailToUpdateAnInvalidCustomerDueToNonExistingCustomer() throws Exception {
        // given
        CustomerDTO updated = new CustomerDTO(97, "NewFirstName", "NewLastName", "Spain");

        // when
        doThrow(CustomerNotFoundException.class).when(client).updateCustomer(updated, 97);

        // then
        mvc.perform(put("/api/customers/97")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());

        verify(client, times(1)).updateCustomer(updated, 97);
    }

    @Test
    void shouldDeleteValidCustomer() throws Exception {
        // when
        doNothing().when(client).deleteCustomer(1);

        // then
        mvc.perform(delete("/api/customers/1")).andExpect(status().isNoContent());

        verify(client, times(1)).deleteCustomer(1);
    }

    @Test
    void shouldFailToDeleteANonExistingCustomer() throws Exception {

        // when
        doThrow(CustomerNotFoundException.class).when(client).deleteCustomer(97);

        // then
        mvc.perform(delete("/api/customers/97")).andExpect(status().isNotFound());

        verify(client, times(1)).deleteCustomer(97);
    }
}
