package com.azure.spring.sample.servicebus;

import com.azure.spring.messaging.ConsumerIdentifier;
import com.azure.spring.messaging.PropertiesSupplier;
import com.azure.spring.messaging.servicebus.core.*;
import com.azure.spring.messaging.servicebus.core.listener.ServiceBusMessageListenerContainer;
import com.azure.spring.messaging.servicebus.core.properties.ProcessorProperties;
import com.azure.spring.messaging.servicebus.core.properties.ProducerProperties;
import com.azure.spring.messaging.servicebus.core.properties.ServiceBusContainerProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.azure.spring.sample.servicebus.IntegrationFlowConfiguration.QUEUE_NAME_1;
import static com.azure.spring.sample.servicebus.IntegrationFlowConfiguration.QUEUE_NAME_2;

@Configuration
@EnableConfigurationProperties(CustomizedServiceBusProperties.class)
public class MultipleAzureServiceBusNamespacesConfiguration {

    private final CustomizedServiceBusProperties properties;

    public MultipleAzureServiceBusNamespacesConfiguration(CustomizedServiceBusProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ServiceBusTemplate firstServiceBusTemplate(ObjectProvider<PropertiesSupplier<String, ProducerProperties>> suppliers) {
        ServiceBusProducerFactory producerFactory = new DefaultServiceBusNamespaceProducerFactory(null, suppliers.getIfAvailable());
        return new ServiceBusTemplate(producerFactory);
    }

    @Bean(name = "firstMessageListenerContainer")
    public ServiceBusMessageListenerContainer firstMessageListenerContainer(ObjectProvider<PropertiesSupplier<ConsumerIdentifier, ProcessorProperties>> suppliers) {
        ServiceBusProcessorFactory processorFactory = new DefaultServiceBusNamespaceProcessorFactory(null, suppliers.getIfAvailable());
        ServiceBusContainerProperties containerProperties = new ServiceBusContainerProperties();
        containerProperties.setEntityName(QUEUE_NAME_1);
        return new ServiceBusMessageListenerContainer(processorFactory, containerProperties);
    }

    @Bean
    public ServiceBusTemplate secondServiceBusTemplate(ObjectProvider<PropertiesSupplier<String, ProducerProperties>> suppliers) {
        ServiceBusProducerFactory producerFactory = new DefaultServiceBusNamespaceProducerFactory(null, suppliers.getIfAvailable());
        return new ServiceBusTemplate(producerFactory);
    }

    @Bean(name = "secondMessageListenerContainer")
    public ServiceBusMessageListenerContainer secondMessageListenerContainer(ObjectProvider<PropertiesSupplier<ConsumerIdentifier, ProcessorProperties>> suppliers) {
        ServiceBusProcessorFactory processorFactory = new DefaultServiceBusNamespaceProcessorFactory(null, suppliers.getIfAvailable());
        ServiceBusContainerProperties containerProperties = new ServiceBusContainerProperties();
        containerProperties.setEntityName(QUEUE_NAME_2);
        return new ServiceBusMessageListenerContainer(processorFactory, containerProperties);
    }

    @Bean
    public PropertiesSupplier<String, ProducerProperties> producerPropertiesSupplier() {
        return properties.producerPropertiesSupplier();
    }

    @Bean
    public PropertiesSupplier<ConsumerIdentifier, ProcessorProperties> processorPropertiesSupplier() {
        return properties.processorPropertiesSupplier();
    }
}
