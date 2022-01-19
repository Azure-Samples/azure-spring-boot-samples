// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import org.apache.qpid.jms.JmsQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.UUID;

/**
 * For reqeust-response pattern of Jms.
 * Premium Tier Only
 */
@RestController
@Profile("sync")
public class SyncQueueSendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncQueueSendController.class);

    @Value("${queuename}")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private ConnectionFactory cf;

    /**
     * For sync sending of message.
     * @param message username as the payload of the message
     * @return User
     * @throws JMSException JMSException
     */
    @PostMapping("/queue/sync")
    public User postMessageAndReceive(@RequestParam String message) throws JMSException {
        LOGGER.info("Sending message");
        jmsTemplate.setReceiveTimeout(1000L);
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);
        Session session = jmsMessagingTemplate.getConnectionFactory().createConnection()
                                              .createSession(false, Session.AUTO_ACKNOWLEDGE);
        ObjectMessage objectMessage = session.createObjectMessage(new User(message));
        objectMessage.setJMSCorrelationID(UUID.randomUUID().toString());
        objectMessage.setJMSReplyTo(cf.createContext().createTemporaryQueue());
        objectMessage.setJMSExpiration(1000L);
        objectMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);

        return jmsMessagingTemplate.convertSendAndReceive(new JmsQueue(queueName),
            objectMessage, User.class); //this operation seems to be blocking + sync
    }
}
