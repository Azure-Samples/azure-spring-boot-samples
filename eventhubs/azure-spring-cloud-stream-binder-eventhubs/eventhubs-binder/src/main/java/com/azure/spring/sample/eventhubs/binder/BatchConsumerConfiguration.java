// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
@Profile("batch")
public class BatchConsumerConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConsumerConfiguration.class);
    private int i = 0;

    @Bean
    public Consumer<List<String>> consume() {
        return list -> list.forEach(event -> LOGGER.info("New event received: '{}'",event));
    }

    @Bean
    public Supplier<Message<String>> supply() {
        return () -> {
            LOGGER.info("Sending message, sequence " + i);
            return MessageBuilder.withPayload("\"Hello world, " + i++ + "\"").build();
        };
    }
}
