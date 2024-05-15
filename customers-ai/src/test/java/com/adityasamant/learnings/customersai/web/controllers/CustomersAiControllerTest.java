package com.adityasamant.learnings.customersai.web.controllers;

import com.adityasamant.learnings.common.domain.customers.CustomersDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomersAiController.class)
class CustomersAiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ChatClient client;

    @Test
    void shouldGenerateCustomersWithDefaultMessage() throws Exception {
        String response = """
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
        String defaultMessage = "Return a list of 3 customers in json format. The fields should contain id, firstName, lastName and country";
        // when
        when(client.call(defaultMessage)).thenReturn(response);

        // then
        mvc.perform(get("/api/customers/plain"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void shouldGenerateCustomersWithPrompt() throws Exception {
        String response = """
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
        BeanOutputParser<CustomersDTO> customerDTOBeanOutputParser = new BeanOutputParser<>(CustomersDTO.class);

        PromptTemplate promptTemplate = new PromptTemplate(
                "Return a list of {count} customers in json format. The fields should contain id, firstName, lastName and country. {format}", Map.of("count", 3, "format", customerDTOBeanOutputParser.getFormat()));
        Prompt prompt = promptTemplate.create();
        // when
        when(client.call(prompt)).thenReturn(chatResponse);

        // then
        mvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}