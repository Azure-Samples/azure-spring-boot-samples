// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example.quickstart.sync;
import com.azure.spring.data.cosmos.example.common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    //This is only a very basic sample app to demonstrate connection using Azure AD. 
    //For more extensive Spring Cosmos samples, check out: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/cosmos/azure-spring-data-cosmos

    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);
    private UserRepository userRepository;

    public SampleApplication(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run(String... var1) {

        final User testUser1 = new User("testId1", "testFirstName", "testLastName1");

        logger.info("Saving user : {}", testUser1);
        userRepository.save(testUser1);

    }
}
