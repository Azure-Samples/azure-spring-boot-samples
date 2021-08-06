// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus.queue.binder.partitionkey;

import com.azure.spring.integration.core.AzureHeaders;
import com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Sinks;

@RestController
public class ServiceBusProducerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusProducerController.class);

    @Autowired
    private Sinks.Many<Message<String>> many;

    /**
     * If the partition key is not set, the default partition key will be used.
     */
    @PostMapping("/notSetPartitionKey")
    public ResponseEntity<String> notSetPartitionKey(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message).build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    /**
     * Set the session id scene.
     */
    @PostMapping("/setSessionId")
    public ResponseEntity<String> setSessionId(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.SESSION_ID, "<custom-session-id>")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    /**
     * Set the ServiceBusMessageHeaders partition key scene.
     */
    @PostMapping("/setServiceBusMessageHeadersPartitionKey")
    public ResponseEntity<String> setServiceBusMessageHeadersPartitionKey(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.PARTITION_KEY, "<custom-partition-key>")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    /**
     * Set the AzureHeaders partition key scene.
     */
    @PostMapping("/setAzureHeadersPartitionKey")
    public ResponseEntity<String> setAzureHeadersPartitionKey(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(AzureHeaders.PARTITION_KEY, "<custom-partition-key>")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    /**
     * Set the session id and ServiceBusMessageHeaders partition key priority scenarios.
     */
    @PostMapping("/setSessionIdAndServiceBusMessageHeadersPartitionKey")
    public ResponseEntity<String> setSessionIdAndServiceBusMessageHeadersPartitionKey(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.SESSION_ID, "<custom-session-id>")
                                    .setHeader(ServiceBusMessageHeaders.PARTITION_KEY, "<custom-partition-key>")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    /**
     * Set session id and AzureHeaders partition key priority scene
     */
    @PostMapping("/setSessionIdAndAzureHeadersPartitionKey")
    public ResponseEntity<String> setSessionIdAndAzureHeadersPartitionKey(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.SESSION_ID, "<custom-session-id>")
                                    .setHeader(AzureHeaders.PARTITION_KEY, "<custom-session-id>")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    /**
     * Set the priority scenarios of AzureHeaders partition key and ServiceBusMessageHeaders partition key.
     */
    @PostMapping("/setAzureHeadersAndServiceBusMessageHeadersPartitionKey")
    public ResponseEntity<String> setAzureHeadersAndServiceBusMessageHeadersPartitionKey(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.PARTITION_KEY, "<custom"
                                        + "-servicebusmessageheader-partition-key>")
                                    .setHeader(AzureHeaders.PARTITION_KEY, "<custom"
                                        + "-azureheaders-partition-key>")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    /**
     * Set the session id scenario and AzureHeaders partition key and ServiceBusMessageHeaders partition key priority
     * scenarios.
     */
    @PostMapping("/setSessionIdAndAzureHeadersAndServiceBusMessageHeadersPartitionKey")
    public ResponseEntity<String> setSessionIdAndAzureHeadersAndServiceBusMessageHeadersPartitionKey(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.SESSION_ID, "<custom-session-id>")
                                    .setHeader(ServiceBusMessageHeaders.PARTITION_KEY, "<custom"
                                        + "-servicebusmessageheader-partition-key>")
                                    .setHeader(AzureHeaders.PARTITION_KEY, "<custom"
                                        + "-azureheaders-partition-key>")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
}
