// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.circuitbreaker;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.exception.CosmosNotFoundException;
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
        Throwable primaryFailure = null;

        try {
            userRepository.save(first);
            userRepository.save(second);

            List<User> synchronousUsers = userRepository.findByFirstName(firstName);
            verifyResults(synchronousUsers, expectedIds);
            LOGGER.info("Synchronous cross-partition query returned {} users.", synchronousUsers.size());

            List<User> reactiveUsers = reactiveUserRepository.findByFirstName(firstName).collectList().block();
            verifyResults(reactiveUsers, expectedIds);
            LOGGER.info("Reactive cross-partition query completed with {} users.", reactiveUsers.size());
        } catch (RuntimeException | Error failure) {
            primaryFailure = failure;
            throw failure;
        } finally {
            cleanUpUsers(primaryFailure, first, second);
        }
    }

    private void cleanUpUsers(Throwable primaryFailure, User... users) {
        Throwable failure = primaryFailure;
        for (User user : users) {
            try {
                userRepository.deleteById(user.getId(), new PartitionKey(user.getLastName()));
            } catch (CosmosNotFoundException alreadyAbsent) {
                LOGGER.debug("Sample user {} is already absent during cleanup.", user.getId());
            } catch (RuntimeException | Error cleanupFailure) {
                if (failure == null) {
                    failure = cleanupFailure;
                } else if (failure != cleanupFailure) {
                    failure.addSuppressed(cleanupFailure);
                }
            }
        }
        if (primaryFailure == null) {
            if (failure instanceof RuntimeException runtimeFailure) {
                throw runtimeFailure;
            }
            if (failure instanceof Error error) {
                throw error;
            }
        }
    }

    private static void verifyResults(List<User> users, Set<String> expectedIds) {
        Set<String> actualIds = users.stream().map(User::getId).collect(Collectors.toSet());
        Assert.state(users.size() == expectedIds.size() && actualIds.equals(expectedIds),
            "The cross-partition query did not return both sample users.");
    }
}
