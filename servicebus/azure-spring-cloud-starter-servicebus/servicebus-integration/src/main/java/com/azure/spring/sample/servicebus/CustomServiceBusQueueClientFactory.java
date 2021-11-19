//package com.azure.spring.sample.servicebus;
//
//import com.azure.messaging.servicebus.ServiceBusClientBuilder;
//import com.azure.messaging.servicebus.ServiceBusErrorContext;
//import com.azure.messaging.servicebus.ServiceBusProcessorClient;
//import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
//import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
//import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
//import com.azure.spring.integration.servicebus.ServiceBusClientConfig;
//import com.azure.spring.integration.servicebus.ServiceBusMessageProcessor;
//import com.azure.spring.integration.servicebus.factory.ServiceBusQueueClientFactory;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class CustomServiceBusQueueClientFactory implements ServiceBusQueueClientFactory {
//
//    private final Map<String, ServiceBusProcessorClient> processorClientMap = new ConcurrentHashMap<>();
//    private final Map<String, ServiceBusSenderAsyncClient> senderClientMap = new ConcurrentHashMap<>();
//    private final ServiceBusClientBuilder serviceBusClientBuilder;
//
//    public CustomServiceBusQueueClientFactory(ServiceBusClientBuilder serviceBusClientBuilder) {
//        this.serviceBusClientBuilder = serviceBusClientBuilder;
//    }
//
//    @Override
//    public ServiceBusProcessorClient getOrCreateProcessor(String name, ServiceBusClientConfig clientConfig,
//           ServiceBusMessageProcessor<ServiceBusReceivedMessageContext, ServiceBusErrorContext> messageProcessor) {
//        return this.processorClientMap.computeIfAbsent(name,
//            n -> createProcessorClient(n, clientConfig, messageProcessor));
//    }
//
//    @Override
//    public ServiceBusSenderAsyncClient getOrCreateSender(String name) {
//        return this.senderClientMap.computeIfAbsent(name, this::createQueueSender);
//    }
//
//    private ServiceBusProcessorClient createProcessorClient(
//        String name,
//        ServiceBusClientConfig clientConfig,
//        ServiceBusMessageProcessor<ServiceBusReceivedMessageContext, ServiceBusErrorContext> messageProcessor) {
//        if (clientConfig.isSessionsEnabled()) {
//            return serviceBusClientBuilder.sessionProcessor()
//                                          .queueName(name)
//                                          .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
//                                          .maxConcurrentCalls(1)
//                                          .maxConcurrentSessions(clientConfig.getConcurrency())
//                                          .prefetchCount(clientConfig.getPrefetchCount())
//                                          .disableAutoComplete()
//                                          .processMessage(messageProcessor.processMessage())
//                                          .processError(messageProcessor.processError())
//                                          .buildProcessorClient();
//
//        } else {
//            return serviceBusClientBuilder.processor()
//                                          .queueName(name)
//                                          .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
//                                          .maxConcurrentCalls(clientConfig.getConcurrency())
//                                          .prefetchCount(clientConfig.getPrefetchCount())
//                                          .disableAutoComplete()
//                                          .processMessage(messageProcessor.processMessage())
//                                          .processError(messageProcessor.processError())
//                                          .buildProcessorClient();
//        }
//
//    }
//
//    private ServiceBusSenderAsyncClient createQueueSender(String name) {
//        return serviceBusClientBuilder.sender().queueName(name).buildAsyncClient();
//    }
//}
