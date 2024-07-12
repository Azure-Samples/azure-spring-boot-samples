// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.queue.messaging;

import com.azure.spring.messaging.storage.queue.core.StorageQueueTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import java.time.Duration;

@SpringBootApplication
public class StorageQueueMessagingApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageQueueMessagingApplication.class);
    private static final String STORAGE_QUEUE_NAME = "test";

    @Autowired
    StorageQueueTemplate storageQueueTemplate;

    public static void main(String[] args) {
        SpringApplication.run(StorageQueueMessagingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        storageQueueTemplate
            .sendAsync(STORAGE_QUEUE_NAME, MessageBuilder.withPayload("Hello world").build())
            .subscribe();
        LOGGER.info("Message was sent successfully.");

        Message<?> message = storageQueueTemplate.receiveAsync(STORAGE_QUEUE_NAME, Duration.ofSeconds(50)).block();
        LOGGER.info("Received message: {}", new String((byte[]) message.getPayload()));
    }

}
