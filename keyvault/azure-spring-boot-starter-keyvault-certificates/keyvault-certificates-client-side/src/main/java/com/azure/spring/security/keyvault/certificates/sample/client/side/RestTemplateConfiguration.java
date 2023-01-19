// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.security.keyvault.certificates.sample.client.side;

import com.azure.security.keyvault.jca.KeyVaultLoadStoreParameter;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.Socket;
import java.security.KeyStore;
import java.util.Map;

@Profile("!webclient")
@Configuration
public class RestTemplateConfiguration {

    private static final String CLIENT_ALIAS = "self-signed";             // It should be your certificate alias used in client-side
    private static final CredentialType CREDENTIALTYPE = CredentialType.ServicePrinciple;

    @Bean
    public RestTemplate restTemplateWithTLS() throws Exception {
        return buildRestTemplateWithTLS(false);
    }

    @Bean
    public RestTemplate restTemplateWithMTLS() throws Exception {
        return buildRestTemplateWithTLS(true);
    }

    private RestTemplate buildRestTemplateWithTLS(boolean enableMtls) throws Exception {
        KeyStore azureKeyVaultKeyStore = buildAzureKeyVaultKeyStore();
        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                .loadTrustMaterial(azureKeyVaultKeyStore, null);
        if (enableMtls) {
            sslContextBuilder.loadKeyMaterial(azureKeyVaultKeyStore, "".toCharArray(), new ClientPrivateKeyStrategy());
        }
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                (hostname, session) -> true);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }

    static KeyStore buildAzureKeyVaultKeyStore() throws Exception {
        KeyStore azureKeyVaultKeyStore = KeyStore.getInstance("AzureKeyVault");
        KeyVaultLoadStoreParameter parameter = null;
        if (CredentialType.ServicePrinciple.equals(CREDENTIALTYPE)) {
            parameter = new KeyVaultLoadStoreParameter(
                    System.getProperty("azure.keyvault.uri"),
                    System.getProperty("azure.keyvault.tenant-id"),
                    System.getProperty("azure.keyvault.client-id"),
                    System.getProperty("azure.keyvault.client-secret"));
        } else if (CredentialType.ManagedIdentity.equals(CREDENTIALTYPE)) {
            parameter = new KeyVaultLoadStoreParameter(
                    System.getProperty("azure.keyvault.uri"),
                    System.getProperty("azure.keyvault.managed-identity"));
        }
        azureKeyVaultKeyStore.load(parameter);
        return azureKeyVaultKeyStore;
    }

    private static class ClientPrivateKeyStrategy implements PrivateKeyStrategy {
        @Override
        public String chooseAlias(Map<String, PrivateKeyDetails> map, Socket socket) {
            return CLIENT_ALIAS;
        }
    }

    enum CredentialType {
        ServicePrinciple,
        ManagedIdentity
    }
}
