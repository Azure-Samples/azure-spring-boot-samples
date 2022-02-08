// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class QueueReceiveService {

    private static final String QUEUE_NAME = "que001";

    private final Logger LOGGER = LoggerFactory.getLogger(QueueReceiveService.class);

    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(User user) {

        LOGGER.info("Received message from queue: {}", user.getName());

    }

}
