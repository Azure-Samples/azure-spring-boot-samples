// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.StopWatch;

@SpringBootApplication
@EnableJms
public class ServiceBusJMSTopicApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusJMSTopicApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusJMSTopicApplication.class, args);
    }

    @Value("${topic}")
    private String TOPIC_NAME;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void run(String... args) throws Exception {
        StopWatch stop = new StopWatch();
        stop.start();
        for (int i = 0; i < 100; i++) {
            LOGGER.info("Sending message: Hello World, " + i);
            String message = "Hello World, " + i;
            jmsTemplate.convertAndSend(TOPIC_NAME, new User(message));
        }
        stop.stop();
        LOGGER.info("total Time is:" + stop.getTotalTimeMillis());
    }
}
