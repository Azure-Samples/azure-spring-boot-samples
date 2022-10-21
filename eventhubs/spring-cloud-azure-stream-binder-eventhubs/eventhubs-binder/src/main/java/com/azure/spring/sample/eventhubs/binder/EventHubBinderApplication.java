// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.ErrorMessage;

import java.util.function.Consumer;

/**
 * @author Warren Zhu
 */
@SpringBootApplication
public class EventHubBinderApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubBinderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EventHubBinderApplication.class, args);
    }

    @Bean
    public Consumer<MessageHandlingException> consumerError() {
        return exception -> LOGGER.error("Handling consumer ERROR", exception);
    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void producerError(ErrorMessage errorMessage) {
        LOGGER.error("Handling Producer ERROR", errorMessage);
    }
}
