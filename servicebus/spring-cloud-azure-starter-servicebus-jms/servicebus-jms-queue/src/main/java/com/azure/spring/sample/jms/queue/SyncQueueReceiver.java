// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * For reqeust-response pattern of Jms.
 * Premium Tier Only
 */
@Component
@Profile("sync")
public class SyncQueueReceiver implements SessionAwareMessageListener<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncQueueReceiver.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Override
    @JmsListener(destination = "${queuename}", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Message message, Session session) throws JMSException {
        User user = (User) ((ObjectMessage) message).getObject();
        LOGGER.info("Received message from queue: {}", user.getName());

        // Message sent back to the replyTo address of the income message.
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), user);
    }


}
