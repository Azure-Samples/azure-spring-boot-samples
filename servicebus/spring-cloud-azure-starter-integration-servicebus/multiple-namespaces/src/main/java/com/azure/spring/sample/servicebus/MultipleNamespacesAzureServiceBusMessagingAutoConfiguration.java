package com.azure.spring.sample.servicebus;

import com.azure.spring.cloud.autoconfigure.servicebus.AzureServiceBusAutoConfiguration;
import com.azure.spring.integration.servicebus.inbound.ServiceBusInboundChannelAdapter;
import com.azure.spring.messaging.ConsumerIdentifier;
import com.azure.spring.messaging.PropertiesSupplier;
import com.azure.spring.messaging.checkpoint.CheckpointConfig;
import com.azure.spring.messaging.checkpoint.CheckpointMode;
import com.azure.spring.messaging.servicebus.core.ServiceBusProcessorFactory;
import com.azure.spring.messaging.servicebus.core.ServiceBusProducerFactory;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.azure.spring.messaging.servicebus.core.listener.ServiceBusMessageListenerContainer;
import com.azure.spring.messaging.servicebus.core.properties.ProcessorProperties;
import com.azure.spring.messaging.servicebus.core.properties.ProducerProperties;
import com.azure.spring.messaging.servicebus.core.properties.ServiceBusContainerProperties;
import com.azure.spring.messaging.servicebus.implementation.core.DefaultServiceBusNamespaceProcessorFactory;
import com.azure.spring.messaging.servicebus.implementation.core.DefaultServiceBusNamespaceProducerFactory;
import com.azure.spring.messaging.servicebus.support.converter.ServiceBusMessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(AzureServiceBusAutoConfiguration.class)
@Import({
    MultipleNamespacesAzureServiceBusMessagingAutoConfiguration.ServiceBusTemplateConfiguration.class,
    MultipleNamespacesAzureServiceBusMessagingAutoConfiguration.ProcessorContainerConfiguration.class
})
public class MultipleNamespacesAzureServiceBusMessagingAutoConfiguration {

    private static final String RECEIVE_QUEUE_NAME = "queue1";
    private static final String INPUT_CHANNEL = "queue1.input";

    @Autowired
    private CustomizedServiceBusProperties properties;

    /**
     * Configure the {@link ServiceBusProcessorFactory}
     */
    @Configuration(proxyBeanMethods = false)
    public static class ProcessorContainerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ServiceBusProcessorFactory defaultServiceBusNamespaceProcessorFactory(
            ObjectProvider<PropertiesSupplier<ConsumerIdentifier, ProcessorProperties>> suppliers) {
            return new DefaultServiceBusNamespaceProcessorFactory(null, suppliers.getIfAvailable());
        }

    }

    /**
     * Configure the {@link ServiceBusTemplate}
     */
    @Configuration(proxyBeanMethods = false)
    public static class ServiceBusTemplateConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ServiceBusProducerFactory defaultServiceBusNamespaceProducerFactory(
            ObjectProvider<PropertiesSupplier<String, ProducerProperties>> suppliers) {
            return new DefaultServiceBusNamespaceProducerFactory(null, suppliers.getIfAvailable());
        }

        @Bean
        @ConditionalOnMissingBean
        public ServiceBusMessageConverter messageConverter() {
            return new ServiceBusMessageConverter();
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnBean(ServiceBusProducerFactory.class)
        public ServiceBusTemplate serviceBusTemplate(ServiceBusProducerFactory senderClientFactory,
                                                     ServiceBusMessageConverter messageConverter) {
            ServiceBusTemplate serviceBusTemplate = new ServiceBusTemplate(senderClientFactory);
            serviceBusTemplate.setMessageConverter(messageConverter);
            return serviceBusTemplate;
        }
    }

    @Bean
    public ServiceBusMessageListenerContainer messageListenerContainer(ServiceBusProcessorFactory processorFactory) {
        ServiceBusContainerProperties containerProperties = new ServiceBusContainerProperties();
        containerProperties.setEntityName(RECEIVE_QUEUE_NAME);
        containerProperties.setCheckpointConfig(new CheckpointConfig(CheckpointMode.MANUAL));
        return new ServiceBusMessageListenerContainer(processorFactory, containerProperties);
    }

    /**
     * {@link ServiceBusInboundChannelAdapter} binding with {@link MessageChannel} has name {@value INPUT_CHANNEL}
     *
     * @param inputChannel the MessageChannel binding with ServiceBusInboundChannelAdapter
     * @param listenerContainer instance of ServiceBusProcessorContainer
     * @return instance of ServiceBusInboundChannelAdapter
     */
    @Bean
    public ServiceBusInboundChannelAdapter queueMessageChannelAdapter(
        @Qualifier(INPUT_CHANNEL) MessageChannel inputChannel,
        ServiceBusMessageListenerContainer listenerContainer) {
        ServiceBusInboundChannelAdapter adapter = new ServiceBusInboundChannelAdapter(listenerContainer);
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    /**
     * {@link MessageChannel} with name {@value INPUT_CHANNEL}
     *
     * @return {@link MessageChannel}
     */
    @Bean(name = INPUT_CHANNEL)
    public MessageChannel input() {
        return new DirectChannel();
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
