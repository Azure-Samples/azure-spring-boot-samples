// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.keyvault.secrets.sample.secret.client;

import com.azure.security.keyvault.secrets.SecretClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecretClientApplication implements CommandLineRunner {

    private final SecretClient secretClient;

    public SecretClientApplication(SecretClient secretClient) {
        this.secretClient = secretClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(SecretClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("sampleProperty: " + secretClient.getSecret("sampleProperty").getValue());
    }
}
