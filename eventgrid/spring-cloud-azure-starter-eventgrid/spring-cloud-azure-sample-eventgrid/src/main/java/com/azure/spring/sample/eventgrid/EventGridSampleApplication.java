// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.sample.eventgrid;

import com.azure.messaging.eventgrid.EventGridEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

@SpringBootApplication
public class EventGridSampleApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventGridSampleApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(EventGridSampleApplication.class, args);
    }

    @Bean
    public Consumer<Message<String>> consume() {
        return message -> {
            List<EventGridEvent> eventData = EventGridEvent.fromString(message.getPayload());
            eventData.forEach(event -> {
                LOGGER.info("New event received: '{}'", event.getData());
            });
        };
    }
}
