// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class QueueSendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueSendController.class);
    private static final String QUEUE_NAME = "dlq-sample";
    private final JmsTemplate jmsTemplate;

    public QueueSendController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/queue")
    public String postMessage(@RequestParam("message") String message) {
        String safeMessage = HtmlUtils.htmlEscape(message);
        LOGGER.info("Sending message: {}", safeMessage);
        jmsTemplate.convertAndSend(QUEUE_NAME, new User(safeMessage));
        return safeMessage;
    }
}
