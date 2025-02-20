// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.keyvault.jca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeyVaultSslBundlesApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyVaultSslBundlesApplication.class, args);
	}

}
