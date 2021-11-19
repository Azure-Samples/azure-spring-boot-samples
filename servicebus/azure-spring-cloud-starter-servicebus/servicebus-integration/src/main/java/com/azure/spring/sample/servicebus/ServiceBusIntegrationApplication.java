// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnAnyProperty;
import com.azure.spring.cloud.autoconfigure.servicebus.properties.AzureServiceBusProperties;
import com.azure.spring.messaging.PropertiesSupplier;
import com.azure.spring.servicebus.core.properties.ProcessorProperties;
import com.azure.spring.servicebus.core.properties.ProducerProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Integration Channel Adapters for Azure Service Bus code sample.
 *
 * @author Warren Zhu
 */
@SpringBootApplication
@EnableIntegration
//@EnableConfigurationProperties(Properties.class)
@Configuration(proxyBeanMethods = false)
public class ServiceBusIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusIntegrationApplication.class, args);
    }

//    @Autowired
//    Properties properties;
//
//    @Bean
//    public PropertiesSupplier<String, ProducerProperties> producerPropertiesSupplier() {
//        return properties.producerPropertiesSupplier();
//    }
//
//    @Bean
//    public PropertiesSupplier<Tuple2<String, String>, ProcessorProperties> processorPropertiesSupplier() {
//        return properties.processorPropertiesSupplier();
//    }

//        ProducerProperties producerProperties = new ProducerProperties();
//        //        AzurePropertiesUtils.copyAzureCommonProperties(properties, producerProperties);
//        //        BeanUtils.copyProperties(properties, producerProperties);
//        BeanUtils.copyProperties(properties.getProducer(), producerProperties);
//        String entityName = producerProperties.getEntityName();
//        return key -> {
//            if (key.equals(entityName)) {
//                return producerProperties;
//            } else {
//                return null;
//            }
//        };
//    }
}
