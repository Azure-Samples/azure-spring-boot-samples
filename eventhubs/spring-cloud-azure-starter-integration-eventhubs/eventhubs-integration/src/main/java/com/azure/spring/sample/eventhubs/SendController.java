// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.eventhubs;

import com.azure.spring.messaging.eventhubs.core.EventHubsTemplate;
import com.azure.spring.integration.core.handler.DefaultMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Warren Zhu
 */
@RestController
public class SendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendController.class);
    private static final String OUTPUT_CHANNEL = "output";
    private static final String EVENTHUB_NAME = "eh1";

    @Autowired
    private EventHubOutboundGateway messagingGateway;

    /**
     * Posts a message to an Azure Event Hub
     */
    @PostMapping("/messages")
    public String send(@RequestParam("message") String message) {
        this.messagingGateway.send(message);
        return message;
    }

  /**
   * This message sender binds with {@link MessagingGateway} via {@link MessageChannel} has name
   * {@value OUTPUT_CHANNEL}
   *
   */
  @Bean
  @ServiceActivator(inputChannel = OUTPUT_CHANNEL)
  public MessageHandler messageSender(EventHubsTemplate eventHubsTemplate) {
        DefaultMessageHandler handler = new DefaultMessageHandler(EVENTHUB_NAME, eventHubsTemplate);
        handler.setSendCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("Message was sent successfully.");
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("There was an error sending the message.", ex);
            }
        });

        return handler;
    }

    /**
     * Message gateway binding with {@link MessageHandler}
     * via {@link MessageChannel} has name {@value OUTPUT_CHANNEL}
     */
    @MessagingGateway(defaultRequestChannel = OUTPUT_CHANNEL)
    public interface EventHubOutboundGateway {
        void send(String text);
    }
}
