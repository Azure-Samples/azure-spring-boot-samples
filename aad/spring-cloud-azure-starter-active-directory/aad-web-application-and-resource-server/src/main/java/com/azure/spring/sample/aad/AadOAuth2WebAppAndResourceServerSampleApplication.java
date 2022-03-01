// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AadOAuth2WebAppAndResourceServerSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AadOAuth2WebAppAndResourceServerSampleApplication.class, args);
    }
}
