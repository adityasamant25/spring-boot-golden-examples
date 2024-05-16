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
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomersFaqController.class)
class CustomersFaqControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ChatClient client;

    @MockBean
    VectorStore vectorStore;

    @Test
    void shouldGenerateValidResponseForDefaultMessage() throws Exception {
        String response =
                "The major buys in the IPL 2024 auction were Pat Cummins bought by Sunrisers Hyderabad for INR 20.5 crore, Mitchell Starc bought by Kolkata Knight Riders for INR 24.75 crore, Daryl Mitchell bought by Chennai Super Kings for INR 14 crore, Rachin Ravindra bought by Chennai Super Kings for INR 1.8 crore, Sameer Rizvi bought by Chennai Super Kings for INR 8.4 crore, Harshal Patel bought by Punjab Kings for INR 11.75 crore, and Alzarri Joseph bought by Royal Challengers Bangalore for INR 11.5 crore.";
        ChatResponse chatResponse = new ChatResponse(List.of(new Generation(response)));

        // when
        when(client.call(new Prompt(""))).thenReturn(chatResponse);

        // then
        mvc.perform(get("/api/customers/ipl2024/faq"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }
}
