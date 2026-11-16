// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.circuitbreaker;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.exception.CosmosNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SampleApplicationTest {
    @Mock
    private UserRepository repository;

    @Mock
    private ReactiveUserRepository reactiveRepository;

    private SampleApplication application;
    private final List<User> attemptedUsers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        application = new SampleApplication(repository, reactiveRepository);
    }

    @Test
    void failedSaveStillCleansBothIdsWithoutHidingTheFailure() {
        RuntimeException saveFailure = new IllegalStateException("Write may have committed");
        RuntimeException cleanupFailure = new IllegalStateException("Cleanup unavailable");
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            attemptedUsers.add(user);
            if (attemptedUsers.size() == 2) {
                throw saveFailure;
            }
            return user;
        });
        doThrow(new CosmosNotFoundException("Already absent", null))
            .doThrow(cleanupFailure).when(repository).deleteById(anyString(), any(PartitionKey.class));

        assertSame(saveFailure, assertThrows(IllegalStateException.class, () -> application.run()));
        assertThat(saveFailure.getSuppressed()).containsExactly(cleanupFailure);
        verifyBothDeletes();
        verifyNoInteractions(reactiveRepository);
    }

    @Test
    void preservesQueryFailureAndBothCleanupFailures() {
        stubSuccessfulSaves();
        RuntimeException queryFailure = new IllegalStateException("Query unavailable");
        RuntimeException firstCleanupFailure = new IllegalStateException("First cleanup failed");
        RuntimeException secondCleanupFailure = new IllegalStateException("Second cleanup failed");
        when(repository.findByFirstName(anyString())).thenThrow(queryFailure);
        doThrow(firstCleanupFailure).doThrow(secondCleanupFailure)
            .when(repository).deleteById(anyString(), any(PartitionKey.class));

        assertSame(queryFailure, assertThrows(IllegalStateException.class, () -> application.run()));
        assertThat(queryFailure.getSuppressed()).containsExactly(firstCleanupFailure, secondCleanupFailure);
        verifyBothDeletes();
        verifyNoInteractions(reactiveRepository);
    }

    @Test
    void reportsCleanupFailuresAfterSuccessfulQueries() {
        stubSuccessfulQueries();
        RuntimeException firstCleanupFailure = new IllegalStateException("First cleanup failed");
        RuntimeException secondCleanupFailure = new IllegalStateException("Second cleanup failed");
        doThrow(firstCleanupFailure).doThrow(secondCleanupFailure)
            .when(repository).deleteById(anyString(), any(PartitionKey.class));

        assertSame(firstCleanupFailure, assertThrows(IllegalStateException.class, () -> application.run()));
        assertThat(firstCleanupFailure.getSuppressed()).containsExactly(secondCleanupFailure);
        verifyBothDeletes();
    }

    @Test
    void toleratesMissingItemsDuringCleanupAfterSuccessfulQueries() {
        stubSuccessfulQueries();
        doThrow(new CosmosNotFoundException("Already absent", null))
            .when(repository).deleteById(anyString(), any(PartitionKey.class));

        application.run();

        verifyBothDeletes();
    }

    private void stubSuccessfulSaves() {
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            attemptedUsers.add(user);
            return user;
        });
    }

    private void stubSuccessfulQueries() {
        stubSuccessfulSaves();
        when(repository.findByFirstName(anyString())).thenAnswer(invocation -> new ArrayList<>(attemptedUsers));
        when(reactiveRepository.findByFirstName(anyString())).thenAnswer(invocation -> Flux.fromIterable(attemptedUsers));
    }

    private void verifyBothDeletes() {
        assertThat(attemptedUsers).hasSize(2);
        for (User user : attemptedUsers) {
            verify(repository).deleteById(user.getId(), new PartitionKey(user.getLastName()));
        }
    }
}
