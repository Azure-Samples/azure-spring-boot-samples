// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Message Producer Mannually.
 */
@RestController
public class QueueSendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueSendController.class);

    @Value("${queuename}")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * @param message username
     * @return message of response
     */
    @PostMapping("/queue")
    public String postMessage(@RequestParam String message) {
        LOGGER.info("Sending message");
        try {
            jmsTemplate.convertAndSend(queueName, new User(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
