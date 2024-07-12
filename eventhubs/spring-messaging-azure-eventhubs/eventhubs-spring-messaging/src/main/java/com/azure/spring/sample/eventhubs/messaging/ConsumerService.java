// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs.messaging;

import com.azure.spring.messaging.eventhubs.implementation.core.annotation.EventHubsListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private static final String EVENT_HUB_NAME = "eh1";
    private static final String CONSUMER_GROUP = "$Default";

    @EventHubsListener(destination = EVENT_HUB_NAME, group = CONSUMER_GROUP)
    public void handleMessageFromEventHub(String message) {
        System.out.printf("New message received: %s%n", message);
    }

}
