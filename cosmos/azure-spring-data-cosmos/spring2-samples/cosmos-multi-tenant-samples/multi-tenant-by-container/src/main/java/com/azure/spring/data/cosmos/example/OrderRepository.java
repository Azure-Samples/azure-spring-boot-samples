// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CosmosRepository<Order, String> {
    //get orders with custom query filtering by type since entities are co-located in same container
    @Query(value = "SELECT c.id, c.orderDetail, c.lastName FROM c where c.type = 'order'")
    List<Order> getAllOrders();
}
