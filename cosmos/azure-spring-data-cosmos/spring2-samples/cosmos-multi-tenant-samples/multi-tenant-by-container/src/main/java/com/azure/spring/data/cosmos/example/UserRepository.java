// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CosmosRepository<User, String> {

    @Query(value = "SELECT c.id, c.firstName, c.lastName, c.orderDetail FROM c")
    Slice<JsonNode> getOrdersAndUsers(Pageable pageable);

    //get orders with custom query filtering by type since entities are co-located in same container
    @Query(value = "SELECT c.id, c.firstName, c.lastName FROM c where c.type = 'user'")
    List<User> getAllUsers();
}
