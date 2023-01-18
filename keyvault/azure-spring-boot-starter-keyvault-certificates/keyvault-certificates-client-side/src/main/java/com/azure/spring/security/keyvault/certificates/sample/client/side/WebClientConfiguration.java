// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.security.keyvault.certificates.sample.client.side;

import com.azure.security.keyvault.jca.KeyVaultKeyManager;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import static com.azure.spring.security.keyvault.certificates.sample.client.side.RestTemplateConfiguration.buildAzureKeyVaultKeyStore;

@Configuration
@Profile("webclient")
public class WebClientConfiguration {

    private static final String CLIENT_ALIAS = "self-signed";    // It should be your certificate alias used in client-side

    @Bean
    public WebClient webClientWithTLS() throws Exception {
        return buildWebClientEnableTls(false);
    }

    @Bean
    public WebClient webClientWithMTLS() throws Exception {
        return buildWebClientEnableTls(true);
    }

    private WebClient buildWebClientEnableTls(boolean enableMtls) throws Exception {
        KeyStore azureKeyVaultKeyStore = buildAzureKeyVaultKeyStore();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(azureKeyVaultKeyStore);

        KeyManager keyManager = enableMtls ? buildKeyManager(azureKeyVaultKeyStore, CLIENT_ALIAS) : null;
        SslContext context = SslContextBuilder.forClient()
                .keyManager(keyManager)
                .trustManager(trustManagerFactory)
                .build();

        HttpClient httpClient = HttpClient.create().secure(sslSpec -> sslSpec.sslContext(context));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private static KeyManager buildKeyManager(KeyStore azureKeyVaultKeyStore, String alias) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(azureKeyVaultKeyStore, "".toCharArray());
        for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
            if (keyManager instanceof KeyVaultKeyManager) {
                return new ConfigurableAliasKeyManager((KeyVaultKeyManager) keyManager, alias);
            }
        }
        return null;
    }


}
