// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.StopWatch;

@SpringBootApplication
@EnableJms
public class ServiceBusJMSQueueApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusJMSQueueApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusJMSQueueApplication.class, args);
    }

    private static final String QUEUE_NAME = "q1";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void run(String... args) {
        StopWatch stop = new StopWatch();
        stop.start();
        for (int i = 0; i < 100; i++) {
            LOGGER.info("Sending message: Hello World, " + i);
            String message = "Hello World, " + i;
            jmsTemplate.convertAndSend(QUEUE_NAME, new User(message));
        }
        stop.stop();
        LOGGER.info("total Time is:" + stop.getTotalTimeMillis());
    }
}
