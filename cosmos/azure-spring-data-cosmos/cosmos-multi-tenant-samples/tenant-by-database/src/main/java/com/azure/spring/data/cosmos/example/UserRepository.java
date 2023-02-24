// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;

@Repository
@ContextConfiguration(classes = AppConfiguration.class)
public interface UserRepository extends CosmosRepository<User, String> {

}
