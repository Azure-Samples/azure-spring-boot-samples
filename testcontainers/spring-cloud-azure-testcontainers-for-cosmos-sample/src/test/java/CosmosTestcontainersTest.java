// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.cosmos.AzureCosmosAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CosmosTestcontainersTest.class)
@Testcontainers
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(classes = { AzureGlobalPropertiesAutoConfiguration.class, AzureCosmosAutoConfiguration.class})
public class CosmosTestcontainersTest {

    @Autowired
    private CosmosClient client;

    @Container
    static CosmosDBEmulatorContainer cosmos = new CosmosDBEmulatorContainer(
        DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest"));

    @DynamicPropertySource
    static void cosmosProperties(DynamicPropertyRegistry registry) throws Exception {
        // Setup SSL certificate before Spring context initializes
        setupSslCertificate();
        
        registry.add("spring.cloud.azure.cosmos.endpoint", cosmos::getEmulatorEndpoint);
        registry.add("spring.cloud.azure.cosmos.key", cosmos::getEmulatorKey);
    }
    
    private static void setupSslCertificate() throws Exception {
        // Create a temporary directory for certificate files
        Path tempDir = Files.createTempDirectory("cosmos-emulator");
        Path certFile = tempDir.resolve("emulator.pem");
        
        // Download the certificate from the emulator using curl (bypassing SSL verification)
        // The emulator exposes its certificate at https://host:8081/_explorer/emulator.pem
        String emulatorHost = cosmos.getHost();
        int emulatorPort = cosmos.getMappedPort(8081);
        String certUrl = String.format("https://%s:%d/_explorer/emulator.pem", emulatorHost, emulatorPort);
        
        // Use curl to download the certificate, bypassing SSL verification
        ProcessBuilder pb = new ProcessBuilder(
            "curl", "-k", "--fail", "--silent", "--show-error",
            "--output", certFile.toString(),
            certUrl
        );
        Process process = pb.start();
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            throw new RuntimeException("Failed to download emulator certificate from " + certUrl);
        }
        
        // Verify the certificate file was created and has content
        if (!Files.exists(certFile) || Files.size(certFile) == 0) {
            throw new RuntimeException("Downloaded certificate file is empty or doesn't exist");
        }
        
        // Create a truststore and import the certificate
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null); // Initialize empty keystore
        
        try (FileInputStream certStream = new FileInputStream(certFile.toFile())) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            Certificate cert = certFactory.generateCertificate(certStream);
            trustStore.setCertificateEntry("cosmos-emulator", cert);
        }
        
        // Save the truststore to a file
        Path keyStoreFile = tempDir.resolve("azure-cosmos-emulator.keystore");
        char[] password = "changeit".toCharArray();
        try (FileOutputStream fos = new FileOutputStream(keyStoreFile.toFile())) {
            trustStore.store(fos, password);
        }

        // Configure JVM to use the truststore
        System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
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
