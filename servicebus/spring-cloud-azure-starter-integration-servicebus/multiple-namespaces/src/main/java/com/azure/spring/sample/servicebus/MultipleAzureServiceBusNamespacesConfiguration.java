package com.azure.spring.sample.servicebus;

import com.azure.spring.messaging.servicebus.core.DefaultServiceBusNamespaceProcessorFactory;
import com.azure.spring.messaging.servicebus.core.DefaultServiceBusNamespaceProducerFactory;
import com.azure.spring.messaging.servicebus.core.ServiceBusProcessorFactory;
import com.azure.spring.messaging.servicebus.core.ServiceBusProducerFactory;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.azure.spring.messaging.servicebus.core.listener.ServiceBusMessageListenerContainer;
import com.azure.spring.messaging.servicebus.core.properties.NamespaceProperties;
import com.azure.spring.messaging.servicebus.core.properties.ServiceBusContainerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomizedServiceBusProperties.class)
public class MultipleAzureServiceBusNamespacesConfiguration {

    private final NamespaceProperties firstNamespaceProperties;
    private final NamespaceProperties secondNamespaceProperties;

    public MultipleAzureServiceBusNamespacesConfiguration(CustomizedServiceBusProperties properties) {
        this.firstNamespaceProperties = properties.getNamespaces().get(0);
        this.secondNamespaceProperties = properties.getNamespaces().get(1);
    }

    @Bean
    public ServiceBusTemplate firstServiceBusTemplate() {
        ServiceBusProducerFactory producerFactory = new DefaultServiceBusNamespaceProducerFactory(firstNamespaceProperties);
        return new ServiceBusTemplate(producerFactory);
    }

    @Bean
    public ServiceBusMessageListenerContainer firstMessageListenerContainer() {
        ServiceBusProcessorFactory processorFactory = new DefaultServiceBusNamespaceProcessorFactory(firstNamespaceProperties);
        ServiceBusContainerProperties containerProperties = new ServiceBusContainerProperties();
        containerProperties.setEntityName(firstNamespaceProperties.getEntityName());
        containerProperties.setPrefetchCount(10);
        return new ServiceBusMessageListenerContainer(processorFactory, containerProperties);
    }

    @Bean
    public ServiceBusTemplate secondServiceBusTemplate() {
        ServiceBusProducerFactory producerFactory = new DefaultServiceBusNamespaceProducerFactory(secondNamespaceProperties);
        return new ServiceBusTemplate(producerFactory);
    }

    @Bean
    public ServiceBusMessageListenerContainer secondMessageListenerContainer() {
        ServiceBusProcessorFactory processorFactory = new DefaultServiceBusNamespaceProcessorFactory(secondNamespaceProperties);
        ServiceBusContainerProperties containerProperties = new ServiceBusContainerProperties();
        containerProperties.setEntityName(secondNamespaceProperties.getEntityName());
        containerProperties.setPrefetchCount(10);
        return new ServiceBusMessageListenerContainer(processorFactory, containerProperties);
    }
}
