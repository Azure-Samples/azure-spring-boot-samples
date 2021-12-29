// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueReceiver {
    private final Logger LOGGER = LoggerFactory.getLogger(QueueReceiver.class);

    @JmsListener(destination = "${queuename}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(User user) {

        LOGGER.info("Received message from queue: {}", user.getName());

    }

}
