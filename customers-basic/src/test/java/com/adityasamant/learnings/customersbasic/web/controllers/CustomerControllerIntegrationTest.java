package com.adityasamant.learnings.customersbasic.web.controllers;


import com.adityasamant.learnings.customersbasic.domain.Customer;
import com.adityasamant.learnings.customersbasic.domain.CustomerCollectionRepository;
import com.adityasamant.learnings.customersbasic.domain.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CustomerCollectionRepository customerCollectionRepository;

    List<Customer> customers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        customers = List.of(new Customer(1, "John", "Doe", "Australia"),
                new Customer(2, "Alice", "Smith", "USA"),
                new Customer(3, "Bob", "Stevens", "England"));
    }

    @Test
    void findAll() throws Exception {
        String jsonResponse = """
                [
                    {
                        "id":1,
                        "firstName":"John",
                        "lastName":"Doe",
                        "country":"Australia"
                    },
                    {
                        "id":2,
                        "firstName":"Alice",
                        "lastName":"Smith",
                        "country":"USA"
                    },
                    {
                        "id":3,
                        "firstName":"Bob",
                        "lastName":"Stevens",
                        "country":"England"
                    }
                ]
                """;
        when(customerCollectionRepository.findAll()).thenReturn(customers);
        mvc.perform(get("/api/customers"))
                .andExpect(status().isOk()).andExpect(content().json(jsonResponse));
    }

    @Test
    void findByValidId() throws Exception {
        when(customerCollectionRepository.findById(1)).thenReturn(Optional.of(customers.getFirst()));

        var customer = customers.getFirst();
        var json = STR."""
        {
            "id":\{customer.id()},
            "firstName":"\{customer.firstName()}",
            "lastName":"\{customer.lastName()}",
            "country":"\{customer.country()}"
        }
        """;
        mvc.perform(get("/api/customers/1")).
                andExpect(status().isOk()).
                andExpect(content().json(json));
    }


    @Test
    void findByInvalidId() throws Exception {

        when(customerCollectionRepository.findById(999)).thenThrow(CustomerNotFoundException.class);

        mvc.perform(get("/api/customers/999")).
                andExpect(status().isNotFound());
    }

    @Test
    void createValidCustomer() throws Exception {
        var customer = new Customer(4, "Trent", "Davids", "Germany");
        doNothing().when(customerCollectionRepository).save(customer);
        var json = STR."""
        {
            "id":\{customer.id()},
            "firstName":"\{customer.firstName()}",
            "lastName":"\{customer.lastName()}",
            "country":"\{customer.country()}"
        }
        """;

        mvc.perform(post("/api/customers").contentType("application/json").content(json)).
                andExpect(status().isCreated());

        verify(customerCollectionRepository,times(1)).save(customer);
    }

    @Test
    void createInvalidCustomer() throws Exception {
        var customer = new Customer(4, "", "Davids", "France");
        doNothing().when(customerCollectionRepository).save(customer);
        var json = STR."""
        {
            "id":\{customer.id()},
            "firstName":"\{customer.firstName()}",
            "lastName":"\{customer.lastName()}",
            "country":"\{customer.country()}"
        }
        """;

        mvc.perform(post("/api/customers").contentType("application/json").content(json)).
                andExpect(status().isBadRequest());

        verify(customerCollectionRepository,times(0)).save(customer);
    }

    @Test
    void updateValidCustomer() throws Exception {
        Customer updated = new Customer(1, "NewFirstName", "NewLastName", "Spain");
        when(customerCollectionRepository.checkInvalidCustomer(1)).thenReturn(false);
        doNothing().when(customerCollectionRepository).save(updated);

        var json = STR."""
        {
            "id":\{updated.id()},
            "firstName":"\{updated.firstName()}",
            "lastName":"\{updated.lastName()}",
            "country":"\{updated.country()}"
        }
        """;

        mvc.perform(put("/api/customers/1").contentType("application/json").content(json)).
                andExpect(status().isNoContent());
        verify(customerCollectionRepository,times(1)).save(updated);
    }

    @Test
    void updateInvalidCustomerDueToBadRequest() throws Exception {
        Customer updated = new Customer(1, "NewFirstName", "", "");
        when(customerCollectionRepository.checkInvalidCustomer(1)).thenReturn(false);
        doNothing().when(customerCollectionRepository).save(updated);

        var json = STR."""
        {
            "id":\{updated.id()},
            "firstName":"\{updated.firstName()}",
            "lastName":"\{updated.lastName()}",
            "country":"\{updated.country()}"
        }
        """;

        mvc.perform(put("/api/customers/1").contentType("application/json").content(json)).
                andExpect(status().isBadRequest());
        verify(customerCollectionRepository,times(0)).save(updated);
    }

    @Test
    void updateInvalidCustomerDueToNonExistingCustomer() throws Exception {
        Customer updated = new Customer(4, "NewFirstName", "NewLastName", "NewCountry");
        when(customerCollectionRepository.checkInvalidCustomer(4)).thenReturn(true);
        doNothing().when(customerCollectionRepository).save(updated);

        var json = STR."""
        {
            "id":\{updated.id()},
            "firstName":"\{updated.firstName()}",
            "lastName":"\{updated.lastName()}",
            "country":"\{updated.country()}"
        }
        """;

        mvc.perform(put("/api/customers/4").contentType("application/json").content(json)).
                andExpect(status().isNotFound());
        verify(customerCollectionRepository,times(0)).save(updated);
    }

    @Test
    void deleteValidCustomer() throws Exception {

        doNothing().when(customerCollectionRepository).delete(1);

        mvc.perform(delete("/api/customers/1")).andExpect(status().isNoContent());

        verify(customerCollectionRepository,times(1)).delete(1);
    }


}