// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.cosmos.springexamples.quickstart.sync;
import com.azure.cosmos.springexamples.common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    //This is only a very basic sample app to demonstrate connection using AAD. 
    //For more extensive Spring Cosmos samples, check out: https://github.com/Azure-Samples/azure-spring-data-cosmos-java-sql-api-samples

    private final Logger logger = LoggerFactory.getLogger(SampleApplication.class);
    private UserRepository userRepository;
    private ReactiveUserRepository reactiveUserRepository;

    public SampleApplication(UserRepository userRepository, ReactiveUserRepository reactiveUserRepository){
        this.reactiveUserRepository = reactiveUserRepository;
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
