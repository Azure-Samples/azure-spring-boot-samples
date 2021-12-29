// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiver {
    private final Logger logger = LoggerFactory.getLogger(TopicReceiver.class);

    @JmsListener(destination = "${topic}", containerFactory = "topicJmsListenerContainerFactory",
            subscription = "${subscription}")
    public void receiveMessage(User user) {

        logger.info("Received message from topic: {}", user.getName());

    }

}
