// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@Configuration(proxyBeanMethods = false)
public class ServiceBusIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusIntegrationApplication.class, args);
    }

}
