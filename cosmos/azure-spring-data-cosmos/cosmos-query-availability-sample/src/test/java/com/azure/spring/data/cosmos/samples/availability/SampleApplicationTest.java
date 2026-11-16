// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.availability;

import com.azure.cosmos.models.PartitionKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SampleApplicationTest {
    @Mock
    private SampleRepository repository;

    @Mock
    private ReactiveSampleRepository reactiveRepository;

    private final List<SampleItem> savedItems = new ArrayList<>();
    private SampleApplication application;

    @BeforeEach
    void setUp() {
        doAnswer(invocation -> {
            SampleItem item = invocation.getArgument(0);
            savedItems.add(item);
            return item;
        }).when(repository).save(any(SampleItem.class));
        application = new SampleApplication(repository, reactiveRepository);
    }

    @Test
    void propagatesQueryFailureAndCleansUpOnlyItsOwnItems() {
        IllegalStateException failure = new IllegalStateException("Query failed");
        when(repository.findByIdPrefix(anyString())).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class, () -> application.run()));

        verifyCleanup();
        verifyNoInteractions(reactiveRepository);
    }

    @Test
    void rejectsIncompleteResultsAndCleansUpItsItems() {
        when(repository.findByIdPrefix(anyString())).thenAnswer(invocation -> List.of(savedItems.get(0)));

        assertThrows(IllegalStateException.class, () -> application.run());

        verifyCleanup();
        verifyNoInteractions(reactiveRepository);
    }

    @Test
    void propagatesReactiveFailureAndCleansUpItsItems() {
        when(repository.findByIdPrefix(anyString())).thenAnswer(invocation -> new ArrayList<>(savedItems));
        IllegalStateException failure = new IllegalStateException("Reactive query failed");
        when(reactiveRepository.findByIdPrefix(anyString())).thenReturn(Flux.error(failure));

        assertSame(failure, assertThrows(IllegalStateException.class, () -> application.run()));

        verifyCleanup();
    }

    private void verifyCleanup() {
        for (SampleItem item : savedItems) {
            verify(repository).deleteById(item.getId(), new PartitionKey(item.getPartitionKey()));
        }
    }
}
