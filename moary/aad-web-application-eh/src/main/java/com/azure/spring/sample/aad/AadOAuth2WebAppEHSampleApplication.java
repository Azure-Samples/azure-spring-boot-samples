// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad;

import com.azure.core.util.IterableStream;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubConsumerClient;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import com.azure.messaging.eventhubs.models.PartitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AadOAuth2WebAppEHSampleApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(AadOAuth2WebAppEHSampleApplication.class);

    @Autowired
    private EventHubProducerClient producerClient;

    @Autowired
    private EventHubConsumerClient consumerClient;

    public static void main(String[] args) {
        SpringApplication.run(AadOAuth2WebAppEHSampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        LOGGER.info("Event Hub producer client created");
//        producerClient.send(Arrays.asList(new EventData("Test event - graalvm")));
//        LOGGER.info("Sent message to Event Hub");
//        producerClient.close();
//
//        TimeUnit.SECONDS.sleep(3);
//        String PARTITION_ID = "0";
//        IterableStream<PartitionEvent> partitionEvents = consumerClient.receiveFromPartition(PARTITION_ID, 1,
//            EventPosition.earliest());
//        Iterator<PartitionEvent> iterator = partitionEvents.stream().iterator();
//        if (iterator.hasNext()) {
//            PartitionEvent pe = iterator.next();
//            LOGGER.info("Received message: {}", pe.getData().getBodyAsString());
//        } else {
//            LOGGER.warn("Failed to receive message.");
//        }
    }
}
