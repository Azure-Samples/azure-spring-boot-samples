// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.security.keyvault.certificates.sample.client.side;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.Socket;
import java.security.KeyStore;
import java.util.Map;

import static com.azure.spring.security.keyvault.certificates.sample.client.side.common.AzureKeyVaultKeyStoreUtil.CLIENT_ALIAS;
import static com.azure.spring.security.keyvault.certificates.sample.client.side.common.AzureKeyVaultKeyStoreUtil.buildAzureKeyVaultKeyStore;

@Configuration
public class RestTemplateConfiguration {

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

    private static class ClientPrivateKeyStrategy implements PrivateKeyStrategy {
        @Override
        public String chooseAlias(Map<String, PrivateKeyDetails> map, Socket socket) {
            return CLIENT_ALIAS;
        }
    }

}
