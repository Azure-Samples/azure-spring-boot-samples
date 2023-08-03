// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class HomeController {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public HomeController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/all")
    public @ResponseBody String getAllTenantUsersAndOrders() {
        Iterable<Order> iterOrder = orderRepository.findAll();
        Iterable<User> iterUser = userRepository.findAll();
        String orders = StreamSupport.stream(iterOrder.spliterator(), true)
                .map(Order::toString)
                .collect(Collectors.joining(" , "));
        String users = StreamSupport.stream(iterUser.spliterator(), true)
                .map(User::toString)
                .collect(Collectors.joining(" , "));
        return orders + users;
    }
}
