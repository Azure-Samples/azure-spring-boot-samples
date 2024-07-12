// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs.messaging;

import com.azure.spring.messaging.eventhubs.core.EventHubsTemplate;
import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableAzureMessaging
public class EventHubMessagingApplication {
    private static final String EVENT_HUB_NAME = "eh1";
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubMessagingApplication.class);


    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(EventHubMessagingApplication.class);
        EventHubsTemplate eventHubsTemplate = applicationContext.getBean(EventHubsTemplate.class);
        TimeUnit.SECONDS.sleep(10);
        LOGGER.info("Sending a message to the Event Hubs.");
        eventHubsTemplate.sendAsync(EVENT_HUB_NAME, MessageBuilder.withPayload("Hello world").build()).subscribe();

    }

}
