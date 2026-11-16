// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.circuitbreaker;

import com.azure.cosmos.models.PartitionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleApplication.class);
    private final UserRepository userRepository;
    private final ReactiveUserRepository reactiveUserRepository;

    public SampleApplication(UserRepository userRepository, ReactiveUserRepository reactiveUserRepository) {
        this.userRepository = userRepository;
        this.reactiveUserRepository = reactiveUserRepository;
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(SampleApplication.class, args)) {
            // Close the client and circuit-breaker background tasks when the sample finishes.
        }
    }

    @Override
    public void run(String... args) {
        String firstName = "ppcb-sample-" + UUID.randomUUID();
        User first = new User(firstName + "-one", firstName, "partition-one");
        User second = new User(firstName + "-two", firstName, "partition-two");
        Set<String> expectedIds = Set.of(first.getId(), second.getId());

        try {
            userRepository.save(first);
            userRepository.save(second);

            List<User> synchronousUsers = userRepository.findByFirstName(firstName);
            verifyResults(synchronousUsers, expectedIds);
            LOGGER.info("Synchronous cross-partition query returned {} users.", synchronousUsers.size());

            List<User> reactiveUsers = reactiveUserRepository.findByFirstName(firstName).collectList().block();
            verifyResults(reactiveUsers, expectedIds);
            LOGGER.info("Reactive cross-partition query completed with {} users.", reactiveUsers.size());
        } finally {
            try {
                userRepository.deleteById(first.getId(), new PartitionKey(first.getLastName()));
            } finally {
                userRepository.deleteById(second.getId(), new PartitionKey(second.getLastName()));
            }
        }
    }

    private static void verifyResults(List<User> users, Set<String> expectedIds) {
        Set<String> actualIds = users.stream().map(User::getId).collect(Collectors.toSet());
        Assert.state(users.size() == expectedIds.size() && actualIds.equals(expectedIds),
            "The cross-partition query did not return both sample users.");
    }
}
