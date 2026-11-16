// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.availability;

import com.azure.spring.data.cosmos.repository.Query;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface ReactiveSampleRepository extends ReactiveCosmosRepository<SampleItem, String> {
    @Query("SELECT * FROM c WHERE STARTSWITH(c.id, @prefix)")
    Flux<SampleItem> findByIdPrefix(@Param("prefix") String prefix);
}
