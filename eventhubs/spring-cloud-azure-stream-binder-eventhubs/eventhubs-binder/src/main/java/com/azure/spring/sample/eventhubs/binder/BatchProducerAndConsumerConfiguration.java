// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs.binder;

import com.azure.spring.messaging.eventhubs.support.EventHubsHeaders;
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
public class BatchProducerAndConsumerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubBinderApplication.class);

    private int i = 0;

    @Bean
    public Supplier<Message<String>> supply() {
        return () -> {
            LOGGER.info("Sending message, sequence " + i++);
            return MessageBuilder.withPayload("\"Hello world " + i++ + "\"").build();
        };
    }

    @Bean
    public Consumer<Message<List<String>>> consume() {
        return message -> {
            for (int i = 0; i < message.getPayload().size(); i++) {
                LOGGER.info("New message received: '{}', partition key: {}, sequence number: {}, offset: {}, enqueued time: {}",
                        message.getPayload().get(i),
                        ((List<Object>) message.getHeaders().get(EventHubsHeaders.BATCH_CONVERTED_PARTITION_KEY)).get(i),
                        ((List<Object>) message.getHeaders().get(EventHubsHeaders.BATCH_CONVERTED_SEQUENCE_NUMBER)).get(i),
                        ((List<Object>) message.getHeaders().get(EventHubsHeaders.BATCH_CONVERTED_OFFSET)).get(i),
                        ((List<Object>) message.getHeaders().get(EventHubsHeaders.BATCH_CONVERTED_ENQUEUED_TIME)).get(i));
            }

        };
    }

}
