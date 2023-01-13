// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.security.keyvault.certificates.sample.client.side;

import com.azure.security.keyvault.jca.KeyVaultLoadStoreParameter;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Map;

@Configuration
public class SampleApplicationConfiguration {

    private final static String ALIAS = "self-signed"; // It should be your certificate alias used in client-side

    @Bean
    public RestTemplate restTemplateWithTLS() throws Exception {
        KeyStore azureKeyVaultKeyStore = buildAzureKeyVaultKeyStore();
        SSLContext sslContext = SSLContexts.custom()
                                           .loadTrustMaterial(azureKeyVaultKeyStore, null)
                                           .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
                                                                                  (hostname, session) -> true);
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setSSLSocketFactory(socketFactory)
                                                    .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }

    @Bean
    public RestTemplate restTemplateWithMTLS() throws Exception {
        KeyStore azureKeyVaultKeyStore = buildAzureKeyVaultKeyStore();
        SSLContext sslContext = SSLContexts.custom()
                                           .loadTrustMaterial(azureKeyVaultKeyStore, null)
                                           .loadKeyMaterial(azureKeyVaultKeyStore, "".toCharArray(), new ClientPrivateKeyStrategy())
                                           .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
            (hostname, session) -> true);
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setSSLSocketFactory(socketFactory)
                                                    .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }

    @Bean
    public WebClient webClientWithTLS() throws Exception {
        KeyStore azureKeyVaultKeyStore = buildAzureKeyVaultKeyStore();
        TrustManagerFactory instance = InsecureTrustManagerFactory.INSTANCE;
        instance.init(azureKeyVaultKeyStore);
        SslContext context = SslContextBuilder.forClient()
                                              .trustManager(instance)
                                              .build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));
        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();

    }

    @Bean
    public WebClient webClientWithMTLS() throws Exception {
        KeyStore azureKeyVaultKeyStore = buildAzureKeyVaultKeyStore();
        TrustManagerFactory instance = InsecureTrustManagerFactory.INSTANCE;
        instance.init(azureKeyVaultKeyStore);
        SslContext context = SslContextBuilder.forClient()
                                              .keyManager((PrivateKey) azureKeyVaultKeyStore.getKey(ALIAS, "".toCharArray()))
                                              .trustManager(instance)
                                              .build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));
        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    private static KeyStore buildAzureKeyVaultKeyStore() throws Exception {
        KeyStore azureKeyVaultKeyStore = KeyStore.getInstance("AzureKeyVault");
        KeyVaultLoadStoreParameter parameter = new KeyVaultLoadStoreParameter(
                System.getProperty("azure.keyvault.uri"),
                System.getProperty("azure.keyvault.tenant-id"),
                System.getProperty("azure.keyvault.client-id"),
                System.getProperty("azure.keyvault.client-secret"));
        azureKeyVaultKeyStore.load(parameter);
        return azureKeyVaultKeyStore;
    }

    private static class ClientPrivateKeyStrategy implements PrivateKeyStrategy {
        @Override
        public String chooseAlias(Map<String, PrivateKeyDetails> map, Socket socket) {
            return ALIAS;
        }
    }
}
