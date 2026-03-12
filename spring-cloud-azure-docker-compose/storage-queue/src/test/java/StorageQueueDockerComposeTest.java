import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.storage.queue.AzureStorageQueueAutoConfiguration;
import com.azure.storage.queue.QueueClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.docker.compose.skip.in-tests=false",
        "spring.docker.compose.file=classpath:storage-compose.yaml",
        "spring.docker.compose.stop.command=down",
        "spring.cloud.azure.storage.queue.queue-name=devstoreaccount1/tc-queue"
})
class StorageQueueDockerComposeTest {

    @Autowired
    private QueueClient queueClient;

    @Test
    void queueClientShouldSendAndReceiveMessage() {
        String message = "Hello World!";
        this.queueClient.create();
        this.queueClient.sendMessage(message);
        var messageItem = this.queueClient.receiveMessage();
        assertThat(messageItem.getBody().toString()).isEqualTo(message);
    }

    @Configuration
    @ImportAutoConfiguration(classes = {
            AzureGlobalPropertiesAutoConfiguration.class,
            AzureStorageQueueAutoConfiguration.class})
    static class Config {
    }

}
