import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.eventhubs.AzureEventHubsAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.eventhubs.AzureEventHubsMessagingAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.storage.blob.properties.AzureStorageBlobConnectionDetails;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.azure.spring.messaging.AzureHeaders.CHECKPOINTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;

@SpringBootTest(properties = {
        "spring.docker.compose.skip.in-tests=false",
        "spring.docker.compose.file=classpath:eventhubs-compose.yaml",
        "spring.docker.compose.stop.command=down",
        "spring.docker.compose.readiness.timeout=PT5M",
        "spring.cloud.function.definition=consume;supply",
        "spring.cloud.stream.bindings.consume-in-0.destination=eh1",
        "spring.cloud.stream.bindings.consume-in-0.group=$Default",
        "spring.cloud.stream.bindings.supply-out-0.destination=eh1",
        "spring.cloud.stream.eventhubs.bindings.consume-in-0.consumer.checkpoint.mode=MANUAL",
        "spring.cloud.stream.poller.fixed-delay=1000",
        "spring.cloud.stream.poller.initial-delay=0"
})
class EventHubsDockerComposeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubsDockerComposeTest.class);
    private static final Set<String> RECEIVED_MESSAGES = ConcurrentHashMap.newKeySet();
    private static final AtomicInteger MESSAGE_SEQUENCE = new AtomicInteger(0);

    @Test
    void supplierAndConsumerShouldWorkThroughEventHub() {
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
        public BlobCheckpointStore blobCheckpointStore(AzureStorageBlobConnectionDetails connectionDetails) {
            BlobServiceAsyncClient blobServiceAsyncClient = new BlobServiceClientBuilder()
                    .connectionString(connectionDetails.getConnectionString())
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