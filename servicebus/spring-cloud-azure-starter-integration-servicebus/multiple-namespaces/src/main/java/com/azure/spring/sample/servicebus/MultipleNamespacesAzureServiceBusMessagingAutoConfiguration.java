package com.azure.spring.sample.servicebus;

import com.azure.spring.cloud.autoconfigure.servicebus.AzureServiceBusAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.servicebus.AzureServiceBusMessagingAutoConfiguration;
import com.azure.spring.messaging.PropertiesSupplier;
import com.azure.spring.servicebus.core.ServiceBusProcessorContainer;
import com.azure.spring.servicebus.core.ServiceBusTemplate;
import com.azure.spring.servicebus.core.processor.DefaultServiceBusNamespaceProcessorFactory;
import com.azure.spring.servicebus.core.processor.ServiceBusProcessorFactory;
import com.azure.spring.servicebus.core.producer.DefaultServiceBusNamespaceProducerFactory;
import com.azure.spring.servicebus.core.producer.ServiceBusProducerFactory;
import com.azure.spring.servicebus.core.properties.ProcessorProperties;
import com.azure.spring.servicebus.core.properties.ProducerProperties;
import com.azure.spring.servicebus.support.converter.ServiceBusMessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.util.function.Tuple2;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(AzureServiceBusAutoConfiguration.class)
@Import({
    MultipleNamespacesAzureServiceBusMessagingAutoConfiguration.ServiceBusTemplateConfiguration.class,
    MultipleNamespacesAzureServiceBusMessagingAutoConfiguration.ProcessorContainerConfiguration.class
})
public class MultipleNamespacesAzureServiceBusMessagingAutoConfiguration {
    /**
     * Configure the {@link ServiceBusProcessorContainer}
     */
    @Configuration(proxyBeanMethods = false)
    public static class ProcessorContainerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ServiceBusProcessorFactory defaultServiceBusNamespaceProcessorFactory(
            ObjectProvider<PropertiesSupplier<Tuple2<String, String>, ProcessorProperties>> suppliers) {
            return new DefaultServiceBusNamespaceProcessorFactory(null, suppliers.getIfAvailable());
        }

        @Bean
        @ConditionalOnMissingBean
        public ServiceBusProcessorContainer messageProcessorContainer(ServiceBusProcessorFactory processorFactory) {
            return new ServiceBusProcessorContainer(processorFactory);
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
        public ServiceBusTemplate serviceBusTemplate(ServiceBusProducerFactory senderClientfactory,
                                                     ServiceBusMessageConverter messageConverter) {
            ServiceBusTemplate serviceBusTemplate = new ServiceBusTemplate(senderClientfactory);
            serviceBusTemplate.setMessageConverter(messageConverter);
            return serviceBusTemplate;
        }
    }
}
