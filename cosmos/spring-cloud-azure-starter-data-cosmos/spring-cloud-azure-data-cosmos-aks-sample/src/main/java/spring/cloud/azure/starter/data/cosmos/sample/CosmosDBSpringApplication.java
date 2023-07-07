// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package spring.cloud.azure.starter.data.cosmos.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;
import java.util.Optional;

@SpringBootApplication
public class CosmosDBSpringApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CosmosDBSpringApplication.class);

    @Autowired
    private UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(CosmosDBSpringApplication.class, args);
    }

    @Override
    public void run(String... var1) {
        this.repository.deleteAll();
        LOGGER.info("Deleted all data in container.");

        final User testUser = new User("testId", "testFirstName", "testLastName", "test address line one");

        // Save the User class to Azure Cosmos DB database.
        final User savedUser = repository.save(testUser);

        Assert.state(savedUser.getFirstName().equals(testUser.getFirstName()), "Saved user first name doesn't match");

        final Optional<User> optionalUserResult = repository.findById(testUser.getEmail());
        Assert.isTrue(optionalUserResult.isPresent(), "Cannot find user.");

        final User result = optionalUserResult.get();
        Assert.state(result.getFirstName().equals(testUser.getFirstName()), "query result firstName doesn't match!");
        Assert.state(result.getLastName().equals(testUser.getLastName()), "query result lastName doesn't match!");

        LOGGER.info("findOne in User collection get result: {}", result.toString());
        LOGGER.info("spring-cloud-azure-data-cosmos-sample successfully run.");
    }
}

