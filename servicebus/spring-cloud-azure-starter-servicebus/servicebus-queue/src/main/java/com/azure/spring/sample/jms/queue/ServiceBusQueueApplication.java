// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.jms.queue;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ServiceBusQueueApplication implements CommandLineRunner {

    @Autowired
    private ServiceBusSenderClient senderClient;

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusQueueApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // send one message to the topic
        senderClient.sendMessage(new ServiceBusMessage("Hello, World!"));
        System.out.println("Sent a message to the queue.");
        senderClient.close();

        // wait the processor client to consume messages
        TimeUnit.SECONDS.sleep(10);
    }
}
