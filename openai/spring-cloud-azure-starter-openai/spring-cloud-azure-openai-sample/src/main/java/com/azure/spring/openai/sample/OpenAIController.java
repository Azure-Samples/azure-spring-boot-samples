// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.openai.sample;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.Choice;
import com.azure.ai.openai.models.Completions;
import com.azure.ai.openai.models.CompletionsOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class OpenAIController {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIController.class);
    public static final String deploymentOrModelId = "gpt-35-turbo";

    private final OpenAIClient client;

    public OpenAIController(OpenAIClient client) {
        this.client = client;
    }

    @PostMapping("/prompt")
    public String send(@RequestBody String prompt) {
        List<String> prompts = Arrays.asList(prompt);
        Completions completions = client.getCompletions(deploymentOrModelId, new CompletionsOptions(prompts));
        String text = null;
        for (Choice choice : completions.getChoices()) {
            text = choice.getText();
            logger.info("Text: {}.", text);
        }
        return String.format("{\"Text\": \"%s\"}", text);
    }
}
