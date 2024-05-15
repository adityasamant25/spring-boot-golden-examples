package com.adityasamant.learnings.customersai.web.controllers;

import com.adityasamant.learnings.common.domain.customers.CustomersDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/customers"))
public class CustomersAiController {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/customer-prompts.st")
    private Resource customersPromptResource;

    @Value("classpath:/prompts/customer-prompts-stuff.st")
    private Resource customersPromptStuffResource;

    @Value("classpath:/prompts/customers-context.st")
    private Resource customersContext;

    @Value("classpath:/docs/xyzcorp-customers.txt")
    private Resource customersDocsResource;

    public CustomersAiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/plain")
    public String generateCustomers(
            @RequestParam(
                            value = "message",
                            defaultValue =
                                    "Return a list of 3 customers in json format. The fields should contain id, firstName, lastName and country")
                    String message) {
        return chatClient.call(message);
    }

    @GetMapping("")
    public CustomersDTO generateCustomersViaPrompt(@RequestParam(value = "count", defaultValue = "3") String count) {

        BeanOutputParser<CustomersDTO> customerDTOBeanOutputParser = new BeanOutputParser<>(CustomersDTO.class);

        PromptTemplate promptTemplate = new PromptTemplate(
                customersPromptResource, Map.of("count", count, "format", customerDTOBeanOutputParser.getFormat()));
        Prompt prompt = promptTemplate.create();
        ChatResponse chatResponse = chatClient.call(prompt);
        Generation generation = chatResponse.getResult();
        return customerDTOBeanOutputParser.parse(generation.getOutput().getContent());
    }

    @GetMapping("/stuff")
    public String generateCustomersViaPromptStuffing(
            @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit) {

        PromptTemplate promptTemplate = new PromptTemplate(customersContext);
        Map<String, Object> map = new HashMap<>();
        map.put("question", customersPromptStuffResource);

        if (stuffit) {
            map.put("context", customersDocsResource);
        } else {
            map.put("context", "");
        }

        Prompt prompt = promptTemplate.create(map);
        ChatResponse response = chatClient.call(prompt);

        return response.getResult().getOutput().getContent();
    }
}
