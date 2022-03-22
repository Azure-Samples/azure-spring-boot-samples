package com.azure.spring.sample.servicebus;

import com.azure.spring.cloud.service.servicebus.properties.ServiceBusEntityType;
import com.azure.spring.integration.core.handler.DefaultMessageHandler;
import com.azure.spring.integration.servicebus.inbound.ServiceBusInboundChannelAdapter;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.azure.spring.messaging.servicebus.core.listener.ServiceBusMessageListenerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class IntegrationFlowConfiguration {

    static final String QUEUE_NAME_1 = "queue1";
    static final String QUEUE_NAME_2 = "queue2";
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationFlowConfiguration.class);


    private final ServiceBusTemplate firstServiceBusTemplate;
    private final ServiceBusTemplate secondServiceBusTemplate;
    private final ServiceBusMessageListenerContainer firstMessageListenerContainer;
    private final ServiceBusMessageListenerContainer secondMessageListenerContainer;

    public IntegrationFlowConfiguration(@Qualifier("firstServiceBusTemplate") ServiceBusTemplate firstServiceBusTemplate,
                                        @Qualifier("secondServiceBusTemplate") ServiceBusTemplate secondServiceBusTemplate,
                                        @Qualifier("firstMessageListenerContainer") ServiceBusMessageListenerContainer firstMessageListenerContainer,
                                        @Qualifier("secondMessageListenerContainer") ServiceBusMessageListenerContainer secondMessageListenerContainer) {
        this.firstServiceBusTemplate = firstServiceBusTemplate;
        this.secondServiceBusTemplate = secondServiceBusTemplate;
        this.firstMessageListenerContainer = firstMessageListenerContainer;
        this.secondMessageListenerContainer = secondMessageListenerContainer;
    }


    @Bean
    public MessageHandler messageSender() {
        secondServiceBusTemplate.setDefaultEntityType(ServiceBusEntityType.QUEUE);
        DefaultMessageHandler handler = new DefaultMessageHandler(QUEUE_NAME_2, secondServiceBusTemplate);
        handler.setSendCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("Message was sent successfully for {}.", QUEUE_NAME_2);
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("There was an error sending the message.");
            }
        });

        return handler;
    }

    @Bean
    public AtomicInteger integerSource() {
        return new AtomicInteger();
    }

    @Bean
    public IntegrationFlow sendFlow() {
        return IntegrationFlows.fromSupplier(integerSource()::getAndIncrement,
                c -> c.poller(Pollers.fixedRate(100)))
                .<Integer, Boolean>route(p -> p % 2 == 0,
                        m -> m.subFlowMapping(true, f -> f.handle(new DefaultMessageHandler(QUEUE_NAME_1, firstServiceBusTemplate)))
                                .subFlowMapping(false, f -> f.handle(new DefaultMessageHandler(QUEUE_NAME_2, secondServiceBusTemplate))))
                .get();
    }

    @Bean
    public IntegrationFlow transformFlow(MessageHandler messageHandler) {
        return IntegrationFlows.from(new ServiceBusInboundChannelAdapter(firstMessageListenerContainer))
                .handle(m -> LOGGER.info("Receive messages from the first queue: {}",  m.getPayload()))
                .transform(i -> i + "transformed")
                .handle(messageHandler)
                .get();
    }

    @Bean
    public IntegrationFlow secondListenerFlow() {
        return IntegrationFlows.from(new ServiceBusInboundChannelAdapter(secondMessageListenerContainer))
                .handle(m -> LOGGER.info("Receive messages from the second queue: {}", m.getPayload()))
                .get();
    }

}
