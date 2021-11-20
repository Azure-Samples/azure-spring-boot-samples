// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs.binder;

import com.azure.spring.eventhubs.support.EventHubsHeaders;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

import static com.azure.spring.messaging.AzureHeaders.CHECKPOINTER;


/**
 * @author Warren Zhu
 */
@SpringBootApplication
public class EventHubBinderApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubBinderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EventHubBinderApplication.class, args);
    }



    // Replace destination with spring.cloud.stream.bindings.consume-in-0.destination
    // Replace group with spring.cloud.stream.bindings.consume-in-0.group
    @ServiceActivator(inputChannel = "{destination}.{group}.errors")
    public void consumerError(Message<?> message) {
        LOGGER.error("Handling customer ERROR: " + message);
    }

    // Replace destination with spring.cloud.stream.bindings.supply-out-0.destination
    @ServiceActivator(inputChannel = "{destination}.errors")
    public void producerError(Message<?> message) {
        LOGGER.error("Handling Producer ERROR: " + message);
    }
}
