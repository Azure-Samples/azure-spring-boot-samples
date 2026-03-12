import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.servicebus.AzureServiceBusAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.servicebus.AzureServiceBusMessagingAutoConfiguration;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.azure.ServiceBusEmulatorContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.azure.spring.messaging.AzureHeaders.CHECKPOINTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;

@SpringJUnitConfig
@TestPropertySource(properties = {
        "spring.cloud.function.definition=consume;supply",
        "spring.cloud.stream.bindings.consume-in-0.destination=queue.1",
        "spring.cloud.stream.bindings.supply-out-0.destination=queue.1",
        "spring.cloud.stream.servicebus.bindings.consume-in-0.consumer.auto-complete=false",
        "spring.cloud.stream.servicebus.bindings.supply-out-0.producer.entity-type=queue",
        "spring.cloud.stream.poller.fixed-delay=1000",
        "spring.cloud.stream.poller.initial-delay=0"})
@Testcontainers
class ServiceBusTestContainerTest {

    private static final Network NETWORK = Network.newNetwork();

    private static final MSSQLServerContainer<?> SQLSERVER = new MSSQLServerContainer<>(
            "mcr.microsoft.com/mssql/server:2022-CU14-ubuntu-22.04")
            .acceptLicense()
            .withNetwork(NETWORK)
            .withNetworkAliases("sqlserver");

    @Container
    @ServiceConnection
    private static final ServiceBusEmulatorContainer SERVICE_BUS = new ServiceBusEmulatorContainer(
            "mcr.microsoft.com/azure-messaging/servicebus-emulator:latest")
            .acceptLicense()
            .withCopyFileToContainer(MountableFile.forClasspathResource("Config.json"),
                    "/ServiceBus_Emulator/ConfigFiles/Config.json")
            .withNetwork(NETWORK)
            .withMsSqlServerContainer(SQLSERVER);

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusTestContainerTest.class);
    private static final Set<String> RECEIVED_MESSAGES = ConcurrentHashMap.newKeySet();
    private static final AtomicInteger MESSAGE_SEQUENCE = new AtomicInteger(0);

    @Test
    void supplierAndConsumerShouldWorkThroughServiceBusQueue() {
        waitAtMost(Duration.ofSeconds(60))
                .pollDelay(Duration.ofSeconds(2))
                .untilAsserted(() -> {
                    assertThat(RECEIVED_MESSAGES).isNotEmpty();
                    LOGGER.info("✓ Test passed - Consumer received {} message(s)", RECEIVED_MESSAGES.size());
                });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @ImportAutoConfiguration(classes = {
            AzureGlobalPropertiesAutoConfiguration.class,
            AzureServiceBusAutoConfiguration.class,
            AzureServiceBusMessagingAutoConfiguration.class})
    static class Config {

        @Bean
        public Supplier<Message<String>> supply() {
            return () -> {
                int sequence = MESSAGE_SEQUENCE.getAndIncrement();
                String payload = "Hello world, " + sequence;
                LOGGER.info("[Supplier] Invoked - message sequence: {}", sequence);
                return MessageBuilder.withPayload(payload).build();
            };
        }

        @Bean
        public Consumer<Message<String>> consume() {
            return message -> {
                String payload = message.getPayload();
                RECEIVED_MESSAGES.add(payload);
                LOGGER.info("[Consumer] Received message: {}", payload);

                Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
                if (checkpointer != null) {
                    checkpointer.success()
                            .doOnSuccess(s -> LOGGER.info("[Consumer] Message checkpointed"))
                            .doOnError(e -> LOGGER.error("[Consumer] Checkpoint failed", e))
                            .block();
                }
            };
        }
    }
}