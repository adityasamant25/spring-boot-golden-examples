package com.adityasamant.learnings.customersai.web.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomersAiController.class)
class CustomersAiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ChatClient client;

    @Test
    void shouldGenerateCustomersWithDefaultMessage() throws Exception {
        String response =
                """
                [
                    {
                        "country": "USA",
                        "firstName": "John",
                        "id": 1,
                        "lastName": "Doe"
                    },
                    {
                        "country": "UK",
                        "firstName": "Jane",
                        "id": 2,
                        "lastName": "Smith"
                    },
                    {
                        "country": "Canada",
                        "firstName": "Peter",
                        "id": 3,
                        "lastName": "Johnson"
                    }
                ]
                """;
        String defaultMessage =
                "Return a list of 3 customers in json format. The fields should contain id, firstName, lastName and country";
        // when
        when(client.call(defaultMessage)).thenReturn(response);

        // then
        mvc.perform(get("/api/customers/plain"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void shouldGenerateCustomersWithPrompt() throws Exception {
        String response =
                """
                 {
                   "customers": [
                     {
                       "id": 1,
                       "firstName": "John",
                       "lastName": "Doe",
                       "country": "USA"
                     },
                     {
                       "id": 2,
                       "firstName": "Jane",
                       "lastName": "Doe",
                       "country": "Canada"
                     },
                     {
                       "id": 3,
                       "firstName": "Jim",
                       "lastName": "Doe",
                       "country": "Mexico"
                     }
                   ]
                 }
                """;

        ChatResponse chatResponse = new ChatResponse(List.of(new Generation(response)));

        // when
        when(client.call(new Prompt(""))).thenReturn(chatResponse);

        // then
        mvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void shouldGenerateDefaultResponseWhenPromptStuffingIsFalse() throws Exception {
        String response = "I'm sorry, but I do not know the answer to that.";
        ChatResponse chatResponse = new ChatResponse(List.of(new Generation(response)));

        // when
        when(client.call(new Prompt(""))).thenReturn(chatResponse);

        // then
        mvc.perform(get("/api/customers/stuff"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    void shouldGenerateValidResponseWhenPromptStuffingIsTrue() throws Exception {
        String response =
                "The customers of XYZ corp as of the year 2024 are John Doe from the USA with the customer id of 1 and Stacy Carter from UK with the customer id of 2.";
        ChatResponse chatResponse = new ChatResponse(List.of(new Generation(response)));

        // when
        when(client.call(new Prompt(""))).thenReturn(chatResponse);

        // then
        mvc.perform(get("/api/customers/stuff?stuffit=true"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }
}
