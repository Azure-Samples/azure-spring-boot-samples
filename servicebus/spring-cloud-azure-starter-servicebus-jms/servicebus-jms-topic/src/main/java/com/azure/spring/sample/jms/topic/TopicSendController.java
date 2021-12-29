// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicSendController {
    private static final Logger logger = LoggerFactory.getLogger(TopicSendController.class);

    @Value("${topic}")
    private String TOPIC_NAME;

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/topic")
    public String postMessage(@RequestParam String message) {

        logger.info("Sending message");

        jmsTemplate.convertAndSend(TOPIC_NAME, new User(message));

        return message;
    }
}
