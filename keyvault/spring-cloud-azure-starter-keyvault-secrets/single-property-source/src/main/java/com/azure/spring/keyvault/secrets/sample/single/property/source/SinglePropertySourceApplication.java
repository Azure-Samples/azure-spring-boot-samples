// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.keyvault.secrets.sample.single.property.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SinglePropertySourceApplication implements CommandLineRunner {

    @Value("${sampleProperty}")
    private String sampleProperty;

    public static void main(String[] args) {
        SpringApplication.run(SinglePropertySourceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("sampleProperty: " + sampleProperty);
    }
}