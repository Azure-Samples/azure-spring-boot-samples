// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.queue.operation;

import com.azure.spring.messaging.AzureHeaders;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import com.azure.spring.messaging.storage.queue.core.StorageQueueTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * @author Miao Cao
 */
@RestController
public class WebController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);
    private static final String STORAGE_QUEUE_NAME = "example";

    @Autowired
    StorageQueueTemplate storageQueueOperation;

    @PostMapping("/messages")
    public String send(@RequestParam("message") String message) {
        this.storageQueueOperation
            .sendAsync(STORAGE_QUEUE_NAME, MessageBuilder.withPayload(message).build())
            .subscribe();
        LOGGER.info("Message {} has been sent successfully.", message);
        return message;
    }

    @GetMapping("/messages")
    public String receive() {
        this.storageQueueOperation.setMessagePayloadType(String.class);
        Message<?> message = this.storageQueueOperation.receiveAsync(STORAGE_QUEUE_NAME, Duration.ofSeconds(30)).block();
        if (message == null) {
            LOGGER.info("You have no new messages.");
            return null;
        }
        LOGGER.info("Message arrived! Payload: " + message.getPayload());

        Checkpointer checkpointer = message.getHeaders().get(AzureHeaders.CHECKPOINTER, Checkpointer.class);
        checkpointer.success()
                    .doOnSuccess(Void -> LOGGER.info("Message '{}' successfully checkpointed", message.getPayload()))
                    .doOnError(e -> LOGGER.error("Fail to checkpoint the message", e))
                    .block();

        return (String) message.getPayload();
    }
}
