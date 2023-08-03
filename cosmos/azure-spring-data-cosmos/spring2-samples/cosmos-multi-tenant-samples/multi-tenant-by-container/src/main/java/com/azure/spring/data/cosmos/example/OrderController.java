// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.cosmos.models.PartitionKey;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public @ResponseBody String createOrder(@RequestBody Order order) {
        UUID uuid = UUID.randomUUID();
        order.setId(String.valueOf(uuid));
        order.setType("order");
        orderRepository.save(order);
        return String.format("Added %s.", order);
    }

    @GetMapping
    public @ResponseBody String getAllOrders() {
        Iterable<Order> iter = orderRepository.getAllOrders();
        return StreamSupport.stream(iter.spliterator(), true)
                .map(Order::toString)
                .collect(Collectors.joining(" , "));
    }

    @GetMapping("/{id}")
    public @ResponseBody String getOrder(@PathVariable UUID id) {
        try {
            Order order = orderRepository.findById(id.toString()).get();
            return order.toString();
        }
        catch (NoSuchElementException e){
            return "No orders found for this tenant!";
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteOrder(@PathVariable UUID id) {
        try {
            Order order = orderRepository.findById(id.toString()).get();
            orderRepository.deleteById(id.toString(), new PartitionKey(order.getLastName()));
            return "Deleted order:" + id;
        }
        catch (NoSuchElementException e){
            return "No orders found for this tenant!";
        }
    }
}
