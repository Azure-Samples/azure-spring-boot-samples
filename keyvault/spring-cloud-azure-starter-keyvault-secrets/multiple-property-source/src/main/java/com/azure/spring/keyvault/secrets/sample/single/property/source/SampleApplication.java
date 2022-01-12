// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.keyvault.secrets.sample.single.property.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    @Value("${sampleProperty1}")
    private String sampleProperty1;
    @Value("${sampleProperty2}")
    private String sampleProperty2;
    @Value("${samplePropertyInMultipleKeyVault}")
    private String samplePropertyInMultipleKeyVault;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run(String[] args) {
        System.out.println("sampleProperty1: " + sampleProperty1);
        System.out.println("sampleProperty2: " + sampleProperty2);
        System.out.println("samplePropertyInMultipleKeyVault: " + samplePropertyInMultipleKeyVault);
    }

}
