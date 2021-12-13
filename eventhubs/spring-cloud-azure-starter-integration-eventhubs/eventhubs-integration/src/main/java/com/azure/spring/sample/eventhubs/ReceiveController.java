// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs;


import com.azure.spring.eventhubs.core.EventHubsProcessorContainer;
import com.azure.spring.integration.eventhubs.inbound.EventHubsInboundChannelAdapter;
import com.azure.spring.messaging.AzureHeaders;
import com.azure.spring.messaging.checkpoint.CheckpointConfig;
import com.azure.spring.messaging.checkpoint.CheckpointMode;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Warren Zhu
 */
@RestController
public class ReceiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveController.class);
    private static final String INPUT_CHANNEL = "input";
    private static final String EVENTHUB_NAME = "eh1";
    private static final String CONSUMER_GROUP = "$Default";

    /**
     * This message receiver binding with {@link EventHubsInboundChannelAdapter}
     * via {@link MessageChannel} has name {@value INPUT_CHANNEL}
     */
    @ServiceActivator(inputChannel = INPUT_CHANNEL)
    public void messageReceiver(byte[] payload, @Header(AzureHeaders.CHECKPOINTER) Checkpointer checkpointer) {
        String message = new String(payload);
        LOGGER.info("New message received: '{}'", message);
        checkpointer.success()
                .doOnSuccess(s -> LOGGER.info("Message '{}' successfully checkpointed", message))
                .doOnError(e -> LOGGER.error("Error found", e))
                .subscribe();
    }

    @Bean
    public EventHubsInboundChannelAdapter messageChannelAdapter(
            @Qualifier(INPUT_CHANNEL) MessageChannel inputChannel,
            EventHubsProcessorContainer processorContainer) {
        CheckpointConfig config = new CheckpointConfig(CheckpointMode.MANUAL);

        EventHubsInboundChannelAdapter adapter =
                new EventHubsInboundChannelAdapter(processorContainer, EVENTHUB_NAME,
                        CONSUMER_GROUP, config);
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @Bean
    public MessageChannel input() {
        return new DirectChannel();
    }
}
