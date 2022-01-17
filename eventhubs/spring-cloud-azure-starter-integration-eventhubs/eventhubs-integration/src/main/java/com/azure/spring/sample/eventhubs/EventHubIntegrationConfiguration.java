// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs;

import com.azure.spring.eventhubs.core.EventHubsProcessorContainer;
import com.azure.spring.integration.eventhubs.inbound.EventHubsInboundChannelAdapter;
import com.azure.spring.messaging.checkpoint.CheckpointConfig;
import com.azure.spring.messaging.checkpoint.CheckpointMode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

/**
 * Configuration Class for EventHubIntegration sample.
 */
@Configuration
public class EventHubIntegrationConfiguration {
    private static final String INPUT_CHANNEL = "input";
    private static final String EVENTHUB_NAME = "eh1";
    private static final String CONSUMER_GROUP = "$Default";

    /**
     * {@link EventHubsInboundChannelAdapter} binding with {@link MessageChannel} has name {@value INPUT_CHANNEL}
     *
     * @param inputChannel the MessageChannel binding with EventHubsInboundChannelAdapter
     * @param processorContainer instance of EventHubsProcessorContainer
     * @return instance of EventHubsInboundChannelAdapter
     */
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

    /**
     * {@link MessageChannel} with name {@value INPUT_CHANNEL}
     *
     * @return {@link MessageChannel}
     */
    @Bean
    public MessageChannel input() {
        return new DirectChannel();
    }

}
