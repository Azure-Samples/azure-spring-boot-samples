package com.azure.spring.dynamicfeatures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DynamicFeaturesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicFeaturesApplication.class, args);
    }

}
