// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.storage.blob.AzureStorageBlobAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.storage.blob.AzureStorageBlobResourceAutoConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = StorageBlobTestcontainersTest.class)
@Testcontainers
@RunWith(SpringRunner.class)
@ImportAutoConfiguration(classes = { AzureGlobalPropertiesAutoConfiguration.class, AzureStorageBlobAutoConfiguration.class, AzureStorageBlobResourceAutoConfiguration.class})
public class StorageBlobTestcontainersTest {
    @Container
    @ServiceConnection
    private static final GenericContainer<?> AZURITE_CONTAINER = new GenericContainer<>(
        "mcr.microsoft.com/azure-storage/azurite:latest")
        .withExposedPorts(10000);

    @Value("azure-blob://testcontainers/message.txt")
    private Resource blobFile;

    @BeforeClass
     public static void setup() {
        AZURITE_CONTAINER.start();
    }

    @Test
    public void test() throws IOException {
        String originalContent = "Hello World!";
        try (OutputStream os = ((WritableResource) this.blobFile).getOutputStream()) {
            os.write(originalContent.getBytes());
        }
        String resultContent = StreamUtils.copyToString(this.blobFile.getInputStream(), Charset.defaultCharset());
        assertThat(resultContent).isEqualTo(originalContent);
    }

}
