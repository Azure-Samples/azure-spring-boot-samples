// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.sample.appconfiguration;

import com.azure.data.appconfiguration.ConfigurationClient;
import com.azure.data.appconfiguration.models.ConfigurationSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppConfigurationClientApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppConfigurationClientApplication.class);

    @Autowired
    private ConfigurationClient configurationClient;

    public static void main(String[] args) {
        SpringApplication.run(AppConfigurationClientApplication.class, args);
    }

    public void run(String... var1) throws Exception {
        String sampleKey = "sample-key";
        ConfigurationSetting configurationSetting = configurationClient.getConfigurationSetting(sampleKey, "somelabel");
        logger.info("Returned the from Azure App Configuration: {}, {}", sampleKey, configurationSetting.getValue());
    }
}
