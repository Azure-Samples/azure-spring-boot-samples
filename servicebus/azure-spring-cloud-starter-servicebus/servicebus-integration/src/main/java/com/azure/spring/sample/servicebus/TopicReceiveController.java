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

/**
 * @author Warren Zhu
 */
@RestController
public class TopicReceiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicReceiveController.class);
    private static final String INPUT_CHANNEL = "topic.input";
    private static final String TOPIC_NAME = "topic1";
    private static final String SUBSCRIPTION_NAME = "group1";

    /**
     * This message receiver binding with {@link ServiceBusInboundChannelAdapter}
     * via {@link MessageChannel} has name {@value INPUT_CHANNEL}
     */
    @ServiceActivator(inputChannel = INPUT_CHANNEL)
    public void messageReceiver(byte[] payload, @Header(AzureHeaders.CHECKPOINTER) Checkpointer checkpointer) {
        String message = new String(payload);
        LOGGER.info("New message received: '{}'", message);
        checkpointer.success().handle((r, ex) -> {
            if (ex == null) {
                LOGGER.info("Message '{}' successfully checkpointed", message);
            }
//            return null;
        });
    }

    @Bean
    public ServiceBusInboundChannelAdapter topicMessageChannelAdapter(
        @Qualifier(INPUT_CHANNEL) MessageChannel inputChannel, ServiceBusProcessorContainer topicOperation) {
        ServiceBusInboundChannelAdapter adapter = new ServiceBusInboundChannelAdapter(topicOperation, TOPIC_NAME, SUBSCRIPTION_NAME,
            new CheckpointConfig(CheckpointMode.MANUAL));
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @Bean(name = INPUT_CHANNEL)
    public MessageChannel input() {
        return new DirectChannel();
    }

}
