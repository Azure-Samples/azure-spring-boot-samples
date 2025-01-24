// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import jakarta.jms.JMSException;
import org.apache.qpid.jms.message.JmsMessageSupport;
import org.apache.qpid.jms.message.JmsObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import static org.apache.qpid.jms.message.JmsMessageSupport.REJECTED;

@Service
public class QueueReceiveService {

    private final Logger LOGGER = LoggerFactory.getLogger(QueueReceiveService.class);
    private static final String QUEUE_NAME = "dlq-sample";
    private static final String DEAD_LETTER_QUEUE_NAME_SUFFIX = "/$deadletterqueue";
    private static final String DEAD_LETTER_QUEUE_NAME = QUEUE_NAME + DEAD_LETTER_QUEUE_NAME_SUFFIX;

    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(JmsObjectMessage message) throws JMSException {
        User user = (User) message.getObject();
        LOGGER.info("Received message from queue: {}.", user);
        if (user.getName().toLowerCase().contains("invalid")) {
            message.setIntProperty(JmsMessageSupport.JMS_AMQP_ACK_TYPE, REJECTED);
            message.acknowledge();
            LOGGER.info("Move message into dead letter queue: {}.", user);
        }
    }

    @JmsListener(destination = DEAD_LETTER_QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveDeadLetterMessage(JmsObjectMessage message) throws JMSException {
        User user = (User) message.getObject();
        LOGGER.info("Received message from dead letter queue: {}.", user);
    }
}
