// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.availability;

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

    private final SampleRepository repository;
    private final ReactiveSampleRepository reactiveRepository;

    public SampleApplication(SampleRepository repository, ReactiveSampleRepository reactiveRepository) {
        this.repository = repository;
        this.reactiveRepository = reactiveRepository;
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(SampleApplication.class, args)) {
            // Closing the context also closes the Cosmos client after the command-line sample finishes.
        }
    }

    @Override
    public void run(String... args) {
        String prefix = "query-availability-" + UUID.randomUUID();
        SampleItem first = new SampleItem(prefix + "-one", "partition-one");
        SampleItem second = new SampleItem(prefix + "-two", "partition-two");
        Set<String> expectedIds = Set.of(first.getId(), second.getId());

        LOGGER.info("Running cross-partition queries for sample items with prefix {}", prefix);
        try {
            repository.save(first);
            repository.save(second);

            List<SampleItem> synchronousItems = repository.findByIdPrefix(prefix);
            verifyResults(synchronousItems, expectedIds);
            LOGGER.info("Synchronous cross-partition query returned {} items.", synchronousItems.size());

            List<SampleItem> reactiveItems = reactiveRepository.findByIdPrefix(prefix).collectList().block();
            verifyResults(reactiveItems, expectedIds);
            LOGGER.info("Reactive cross-partition query completed with {} items.", reactiveItems.size());
        } finally {
            try {
                repository.deleteById(first.getId(), new PartitionKey(first.getPartitionKey()));
            } finally {
                repository.deleteById(second.getId(), new PartitionKey(second.getPartitionKey()));
            }
        }
    }

    private static void verifyResults(List<SampleItem> items, Set<String> expectedIds) {
        Set<String> actualIds = items.stream().map(SampleItem::getId).collect(Collectors.toSet());
        Assert.state(items.size() == expectedIds.size() && actualIds.equals(expectedIds),
            "The cross-partition query did not return both sample items.");
    }
}
