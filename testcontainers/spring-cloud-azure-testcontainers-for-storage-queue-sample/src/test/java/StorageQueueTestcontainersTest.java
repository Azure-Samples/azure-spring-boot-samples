// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.storage.queue.AzureStorageQueueAutoConfiguration;
import com.azure.storage.queue.QueueClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.springframework.test.context.TestPropertySource;

import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = StorageQueueTestcontainersTest.class)
@Testcontainers
@TestPropertySource(properties = "spring.cloud.azure.storage.queue.queue-name=devstoreaccount1/tc-queue")
@RunWith(SpringRunner.class)
@ImportAutoConfiguration(classes = { AzureGlobalPropertiesAutoConfiguration.class, AzureStorageQueueAutoConfiguration.class})
public class StorageQueueTestcontainersTest {

    @Container
    @ServiceConnection
    private static final GenericContainer<?> AZURITE_CONTAINER = new GenericContainer<>(
        "mcr.microsoft.com/azure-storage/azurite:latest")
        .withExposedPorts(10001);

    @Autowired
    private QueueClient queueClient;

    @BeforeClass
    public static void setup() {
        AZURITE_CONTAINER.start();
    }

    @Test
    public void test() {
        String message = "Hello World!";
        this.queueClient.create();
        this.queueClient.sendMessage(message);
        assertThat(this.queueClient.receiveMessage().getBody().toString()).isEqualTo(message);
    }

}
