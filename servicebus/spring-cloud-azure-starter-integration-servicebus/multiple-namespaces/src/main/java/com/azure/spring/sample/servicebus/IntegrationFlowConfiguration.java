package com.azure.spring.sample.servicebus;

import com.azure.spring.integration.core.handler.DefaultMessageHandler;
import com.azure.spring.integration.servicebus.inbound.ServiceBusInboundChannelAdapter;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.azure.spring.messaging.servicebus.core.listener.ServiceBusMessageListenerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.MessageHandler;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class IntegrationFlowConfiguration {

    @Value("${my.servicebus.namespaces[0].entity-name:}")
    private String firstQueueName;

    @Value("${my.servicebus.namespaces[1].entity-name:}")
    private String secondQueueName;

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
    public MessageHandler firstMessageHandler() {
        return new DefaultMessageHandler(firstQueueName, firstServiceBusTemplate);
    }

    @Bean
    public ServiceBusInboundChannelAdapter firstServiceBusInboundChannelAdapter() {
        ServiceBusInboundChannelAdapter channelAdapter = new ServiceBusInboundChannelAdapter(firstMessageListenerContainer);
        channelAdapter.setPayloadType(String.class);
        return channelAdapter;
    }

    @Bean
    public MessageHandler secondMessageHandler() {
        return new DefaultMessageHandler(secondQueueName, secondServiceBusTemplate);
    }

    @Bean
    public ServiceBusInboundChannelAdapter secondServiceBusInboundChannelAdapter() {
        ServiceBusInboundChannelAdapter channelAdapter = new ServiceBusInboundChannelAdapter(secondMessageListenerContainer);
        channelAdapter.setPayloadType(String.class);
        return channelAdapter;
    }

    @Bean
    public AtomicInteger integerSource() {
        return new AtomicInteger();
    }

    @Bean
    public IntegrationFlow sendFlow() {
        return IntegrationFlows.fromSupplier(integerSource()::getAndIncrement,
                c -> c.poller(Pollers.fixedRate(Duration.ofSeconds(10))))
                .<Integer, Boolean>route(p -> p % 2 == 0, m
                        -> m.subFlowMapping(true, f -> f.handle(firstMessageHandler()))
                        .subFlowMapping(false, f -> f.handle(secondMessageHandler())))
                .get();
    }

    @Bean
    public IntegrationFlow transformFlow() {
        return IntegrationFlows.from(firstServiceBusInboundChannelAdapter())
                .transform(m -> {
                    LOGGER.info("Receive messages from the first queue: {}", m);
                    return "transformed from queue1, " + m;
                })
                .handle(secondMessageHandler())
                .get();
    }

    @Bean
    public IntegrationFlow secondListenerFlow() {
        return IntegrationFlows.from(secondServiceBusInboundChannelAdapter())
                .handle(m -> LOGGER.info("Receive messages from the second queue: {}", m.getPayload()))
                .get();
    }
}
