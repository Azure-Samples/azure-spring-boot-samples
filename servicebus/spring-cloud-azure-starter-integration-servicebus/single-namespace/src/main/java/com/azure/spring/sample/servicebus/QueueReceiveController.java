// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import com.azure.spring.integration.servicebus.inbound.ServiceBusInboundChannelAdapter;
import com.azure.spring.messaging.AzureHeaders;
import com.azure.spring.messaging.checkpoint.CheckpointConfig;
import com.azure.spring.messaging.checkpoint.CheckpointMode;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import com.azure.spring.servicebus.core.ServiceBusProcessorContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueReceiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueReceiveController.class);
    private static final String INPUT_CHANNEL = "queue.input";
    private static final String QUEUE_NAME = "queue1";

    /**
     * This message receiver binding with {@link ServiceBusInboundChannelAdapter}
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
    public ServiceBusInboundChannelAdapter queueMessageChannelAdapter(
        @Qualifier(INPUT_CHANNEL) MessageChannel inputChannel, ServiceBusProcessorContainer processorContainer) {
        ServiceBusInboundChannelAdapter adapter = new ServiceBusInboundChannelAdapter(processorContainer, QUEUE_NAME,
                null, new CheckpointConfig(CheckpointMode.MANUAL));
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @Bean(name = INPUT_CHANNEL)
    public MessageChannel input() {
        return new DirectChannel();
    }
}
