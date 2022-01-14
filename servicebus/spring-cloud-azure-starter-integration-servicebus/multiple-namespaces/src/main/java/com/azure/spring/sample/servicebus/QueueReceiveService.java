// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import com.azure.spring.integration.handler.DefaultMessageHandler;
import com.azure.spring.integration.servicebus.inbound.ServiceBusInboundChannelAdapter;
import com.azure.spring.messaging.AzureHeaders;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import com.azure.spring.service.servicebus.properties.ServiceBusEntityType;
import com.azure.spring.servicebus.core.ServiceBusTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class QueueReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueReceiveService.class);
    private static final String INPUT_CHANNEL = "queue1.input";
    private static final String OUTPUT_CHANNEL_QUEUE2 = "queue2.output";
    private static final String FORWARD_QUEUE_NAME = "queue2";

    @Autowired
    private QueueForwardGateway messagingGateway;

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
        this.messagingGateway.send(message);
    }


    /**
     * Get messages from {@link MessageChannel} with name {@value OUTPUT_CHANNEL_QUEUE2}
     * and send messages to queue with name {@value FORWARD_QUEUE_NAME}.
     *
     * @param serviceBusTemplate template to send messages
     * @return instance of {@link MessageChannel}
     */
    @Bean
    @ServiceActivator(inputChannel = OUTPUT_CHANNEL_QUEUE2)
    public MessageHandler queueMessageForwarder(ServiceBusTemplate serviceBusTemplate) {
        serviceBusTemplate.setDefaultEntityType(ServiceBusEntityType.QUEUE);
        DefaultMessageHandler handler = new DefaultMessageHandler(FORWARD_QUEUE_NAME, serviceBusTemplate);
        handler.setSendCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("Message was sent successfully for {}.", FORWARD_QUEUE_NAME);
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("There was an error sending the message.");
            }
        });

        return handler;
    }

    /**
     * Message gateway binding with {@link MessageHandler}
     * via {@link MessageChannel} has name {@value OUTPUT_CHANNEL_QUEUE2}
     */
    @MessagingGateway(defaultRequestChannel = OUTPUT_CHANNEL_QUEUE2)
    public interface QueueForwardGateway {
        void send(String text);
    }
}
