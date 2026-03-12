import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.eventhubs.AzureEventHubsAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.eventhubs.properties.AzureEventHubsConnectionDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;

@SpringBootTest(properties = {
        "spring.docker.compose.skip.in-tests=false",
        "spring.docker.compose.file=classpath:eventhubs-compose.yaml",
        "spring.docker.compose.stop.command=down",
        "spring.docker.compose.readiness.timeout=PT5M",
        "spring.cloud.azure.eventhubs.event-hub-name=eh1",
        "spring.cloud.azure.eventhubs.producer.event-hub-name=eh1"
})
class EventHubsDockerComposeTest {

    @Autowired
    private AzureEventHubsConnectionDetails connectionDetails;

    @Autowired
    private EventHubProducerClient producerClient;

    @Test
    void connectionDetailsShouldBeProvidedByFactory() {
        assertThat(connectionDetails).isNotNull();
        assertThat(connectionDetails.getConnectionString())
                .isNotBlank()
                .startsWith("Endpoint=sb://");
    }

    @Test
    void producerClientCanSendMessage() {
        // Wait for Event Hubs emulator to be fully ready and event hub entity to be available
        waitAtMost(Duration.ofSeconds(120)).pollInterval(Duration.ofSeconds(2)).untilAsserted(() -> {
            EventData event = new EventData("Hello World!");
            this.producerClient.send(Collections.singletonList(event));
        });
    }

    @Configuration(proxyBeanMethods = false)
    @ImportAutoConfiguration(classes = {
            AzureGlobalPropertiesAutoConfiguration.class,
            AzureEventHubsAutoConfiguration.class})
    static class Config {
    }
}