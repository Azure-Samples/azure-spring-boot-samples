import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.eventhubs.AzureEventHubsAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.eventhubs.AzureEventHubsMessagingAutoConfiguration;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobServiceVersion;
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
import org.testcontainers.azure.AzuriteContainer;
import org.testcontainers.azure.EventHubsEmulatorContainer;
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
        "spring.cloud.stream.bindings.consume-in-0.destination=eh1",
        "spring.cloud.stream.bindings.consume-in-0.group=$Default",
        "spring.cloud.stream.bindings.supply-out-0.destination=eh1",
        "spring.cloud.stream.eventhubs.bindings.consume-in-0.consumer.checkpoint.mode=MANUAL",
        "spring.cloud.stream.poller.fixed-delay=1000",
        "spring.cloud.stream.poller.initial-delay=0"})
@Testcontainers
class EventHubsTestContainerTest {

    private static final Network NETWORK = Network.newNetwork();

    private static final AzuriteContainer AZURITE = new AzuriteContainer(
            "mcr.microsoft.com/azure-storage/azurite:latest")
            .withCommand("azurite", "--blobHost", "0.0.0.0", "--queueHost", "0.0.0.0", "--tableHost", "0.0.0.0",
                    "--skipApiVersionCheck")
            .withNetwork(NETWORK)
            .withNetworkAliases("azurite");

    @Container
    @ServiceConnection
    private static final EventHubsEmulatorContainer EVENT_HUBS = new EventHubsEmulatorContainer(
            "mcr.microsoft.com/azure-messaging/eventhubs-emulator:latest")
            .acceptLicense()
            .withCopyFileToContainer(MountableFile.forClasspathResource("Config.json"),
                    "/Eventhubs_Emulator/ConfigFiles/Config.json")
            .withNetwork(NETWORK)
            .withAzuriteContainer(AZURITE);

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubsTestContainerTest.class);
    private static final Set<String> RECEIVED_MESSAGES = ConcurrentHashMap.newKeySet();
    private static final AtomicInteger messageSequence = new AtomicInteger(0);

    @Test
    void supplierAndConsumerShouldWorkThroughEventHubs() {
        waitAtMost(Duration.ofSeconds(120))
                .pollDelay(Duration.ofSeconds(2))
                .pollInterval(Duration.ofSeconds(2))
                .untilAsserted(() -> {
                    assertThat(RECEIVED_MESSAGES).isNotEmpty();
                    LOGGER.info("✓ Test passed - Consumer received {} message(s)", RECEIVED_MESSAGES.size());
                });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @ImportAutoConfiguration(classes = {
            AzureGlobalPropertiesAutoConfiguration.class,
            AzureEventHubsAutoConfiguration.class,
            AzureEventHubsMessagingAutoConfiguration.class})
    static class Config {

        private static final String CHECKPOINT_CONTAINER_NAME = "eventhubs-checkpoint";

        @Bean
        public BlobCheckpointStore blobCheckpointStore() {
            BlobServiceAsyncClient blobServiceAsyncClient = new BlobServiceClientBuilder()
                    .connectionString(AZURITE.getConnectionString())
                    .serviceVersion(BlobServiceVersion.V2025_01_05)
                    .buildAsyncClient();
            BlobContainerAsyncClient containerAsyncClient = blobServiceAsyncClient
                    .getBlobContainerAsyncClient(CHECKPOINT_CONTAINER_NAME);
            if (Boolean.FALSE.equals(containerAsyncClient.exists().block(Duration.ofSeconds(3)))) {
                containerAsyncClient.create().block(Duration.ofSeconds(3));
            }
            return new BlobCheckpointStore(containerAsyncClient);
        }

        @Bean
        public Supplier<Message<String>> supply() {
            return () -> {
                int sequence = messageSequence.getAndIncrement();
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