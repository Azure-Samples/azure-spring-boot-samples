// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import com.azure.spring.sample.servicebus.QueueSendService.QueueSendGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@EnableConfigurationProperties(CustomizedServiceBusProperties.class)
@Configuration(proxyBeanMethods = false)
public class ServiceBusIntegrationApplication implements CommandLineRunner {

    @Autowired
    private QueueSendGateway messagingGateway;

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusIntegrationApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        this.messagingGateway.send("hello");
    }

}
