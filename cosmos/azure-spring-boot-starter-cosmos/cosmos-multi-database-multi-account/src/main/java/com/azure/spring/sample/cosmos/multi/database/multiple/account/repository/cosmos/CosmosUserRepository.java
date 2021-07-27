// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.cosmos.multi.database.multiple.account.repository.cosmos;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CosmosUserRepository extends ReactiveCosmosRepository<CosmosUser, String> {
}
