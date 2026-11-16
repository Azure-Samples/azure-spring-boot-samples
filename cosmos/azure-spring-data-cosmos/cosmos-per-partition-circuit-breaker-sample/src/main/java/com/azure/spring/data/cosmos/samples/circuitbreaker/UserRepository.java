// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.circuitbreaker;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

import java.util.List;

public interface UserRepository extends CosmosRepository<User, String> {
    List<User> findByFirstName(String firstName);
}
