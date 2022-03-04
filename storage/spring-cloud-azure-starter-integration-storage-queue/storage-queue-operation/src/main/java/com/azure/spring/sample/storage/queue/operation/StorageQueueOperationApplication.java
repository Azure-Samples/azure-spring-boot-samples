// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.queue.operation;

import com.azure.spring.messaging.storage.queue.core.StorageQueueTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * {@link StorageQueueTemplate} code sample.
 *
 * @author Miao Cao
 */
@SpringBootApplication
public class StorageQueueOperationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageQueueOperationApplication.class, args);
    }
}
