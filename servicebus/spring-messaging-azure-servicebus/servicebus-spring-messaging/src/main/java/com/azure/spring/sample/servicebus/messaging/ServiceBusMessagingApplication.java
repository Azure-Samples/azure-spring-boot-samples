// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus.messaging;


import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
@EnableAzureMessaging
public class ServiceBusMessagingApplication {
    private static final String QUEUE_NAME = "que001";

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ServiceBusMessagingApplication.class);
        ServiceBusTemplate serviceBusTemplate = applicationContext.getBean(ServiceBusTemplate.class);
        System.out.println("Sending a message to the queue.");
        serviceBusTemplate.sendAsync(QUEUE_NAME, MessageBuilder.withPayload("Hello world").build()).subscribe();
    }

}
