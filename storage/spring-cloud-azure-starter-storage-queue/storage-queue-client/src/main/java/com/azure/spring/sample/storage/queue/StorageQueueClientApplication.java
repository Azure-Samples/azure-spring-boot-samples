// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.queue;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.azure.storage.queue.models.SendMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Send and receive message using Storage Queue SDK client.
 */
@SpringBootApplication
public class StorageQueueClientApplication implements CommandLineRunner {

    final static Logger logger = LoggerFactory.getLogger(StorageQueueClientApplication.class);

    @Autowired
    private QueueClient queueClient;

    public static void main(String[] args) {
        SpringApplication.run(StorageQueueClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        queueClient.create();
        SendMessageResult sendMessageResult = queueClient.sendMessage("test");
        logger.info("Send message id: {}", sendMessageResult.getMessageId());

        QueueMessageItem queueMessageItem = queueClient.receiveMessage();
        logger.info("Received message: {}", new String(queueMessageItem.getBody().toBytes()));
    }
}
