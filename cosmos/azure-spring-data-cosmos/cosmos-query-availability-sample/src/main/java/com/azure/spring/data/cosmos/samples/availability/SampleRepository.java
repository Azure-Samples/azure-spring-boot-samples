// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.availability;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SampleRepository extends CosmosRepository<SampleItem, String> {
    @Query("SELECT * FROM c WHERE STARTSWITH(c.id, @prefix)")
    List<SampleItem> findByIdPrefix(@Param("prefix") String prefix);
}
