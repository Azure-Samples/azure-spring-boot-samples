// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.passwordless.postgresql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostgresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostgresqlApplication.class, args);
    }

}
