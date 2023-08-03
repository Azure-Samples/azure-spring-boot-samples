// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.spring.data.cosmos.core.query.CosmosPageRequest;
import com.azure.spring.data.cosmos.example.tenant.TenantStorage;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final TenantStorage tenantStorage;
    private final UserRepository userRepository;
    private final Environment env;

    public HomeController(TenantStorage tenantStorage, UserRepository userRepository, Environment env) {
        this.tenantStorage = tenantStorage;
        this.userRepository = userRepository;
        this.env = env;
    }

    @GetMapping(path = "/all")
    public @ResponseBody String getAllTenantUsersAndOrders() {
        //because order and user types are co-located with same partition key, we can hit the container once for both
        //and return the results in a single json object (can split into entities based on type later if needed)
        return userRepository.getOrdersAndUsers(CosmosPageRequest.of(0, 10))
                .toList()
                .toString();
    }
}
