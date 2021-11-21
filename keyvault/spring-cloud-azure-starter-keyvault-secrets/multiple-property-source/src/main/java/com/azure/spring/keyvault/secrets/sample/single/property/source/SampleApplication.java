// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.keyvault.secrets.sample.single.property.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    @Value("${secret-name-in-key-vault-1}")
    private String secretNameInKeyVault1;
    @Value("${secret-name-in-key-vault-2}")
    private String secretNameInKeyVault2;
    @Value("${secret-name-in-key-vault-both}")
    private String secretNameInKeyVaultBoth;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run(String[] args) {
        System.out.println("secretNameInKeyVault1: " + secretNameInKeyVault1);
        System.out.println("secretNameInKeyVault2: " + secretNameInKeyVault2);
        System.out.println("secretNameInKeyVaultBoth: " + secretNameInKeyVaultBoth);
    }

}
