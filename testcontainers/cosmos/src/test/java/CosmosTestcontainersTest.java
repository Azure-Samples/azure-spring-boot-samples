// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.cosmos.AzureCosmosAutoConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CosmosTestcontainersTest.class)
@Testcontainers
@RunWith(SpringRunner.class)
@ImportAutoConfiguration(classes = { AzureGlobalPropertiesAutoConfiguration.class, AzureCosmosAutoConfiguration.class})
public class CosmosTestcontainersTest {

    @TempDir
    private static File tempFolder;

    @Autowired
    private CosmosClient client;

    @Container
    @ServiceConnection
    static CosmosDBEmulatorContainer cosmos = new CosmosDBEmulatorContainer(
        DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest"));

    @BeforeClass
    public static void setup() {
        cosmos.start();
        Path keyStoreFile = new File(tempFolder, "azure-cosmos-emulator.keystore").toPath();
        KeyStore keyStore = cosmos.buildNewKeyStore();
        try {
            keyStore.store(Files.newOutputStream(keyStoreFile.toFile().toPath()), cosmos.getEmulatorKey().toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", cosmos.getEmulatorKey());
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
    }

    @Test
    public void test() {
        CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists("Azure");
        assertThat(databaseResponse.getStatusCode()).isEqualTo(201);
        CosmosContainerResponse containerResponse = client
            .getDatabase("Azure")
            .createContainerIfNotExists("ServiceContainer", "/name");
        assertThat(containerResponse.getStatusCode()).isEqualTo(201);
    }

}
