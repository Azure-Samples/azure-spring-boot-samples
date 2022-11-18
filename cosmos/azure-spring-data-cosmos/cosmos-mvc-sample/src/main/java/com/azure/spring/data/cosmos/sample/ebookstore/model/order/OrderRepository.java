// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.model.order;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface OrderRepository extends CosmosRepository<Order, String> {
    @Query(value = "select * from c where c.customerId= @customerId ORDER BY c._ts DESC")
    List<Order> getOrdersByCustomerIdOrderByTimestamp(String customerId);
}
