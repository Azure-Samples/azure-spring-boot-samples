// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example.quickstart.sync;

import com.azure.spring.data.cosmos.example.common.User;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReactiveUserRepository extends ReactiveCosmosRepository<User, String> {

    Flux<User> findByFirstName(String firstName);
}

