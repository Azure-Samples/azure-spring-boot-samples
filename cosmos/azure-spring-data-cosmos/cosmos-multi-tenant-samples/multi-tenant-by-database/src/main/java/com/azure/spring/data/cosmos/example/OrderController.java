// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.example.tenant.TenantStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderRepository orderRepository;
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public @ResponseBody String createUser(@RequestBody Order order) {
        UUID uuid = UUID.randomUUID();
        order.setId(String.valueOf(uuid));
        orderRepository.save(order);
        return String.format("Added %s.", order);
    }

    @GetMapping
    public @ResponseBody String getAllOrders() {
        Iterable<Order> iter = orderRepository.findAll();
        return StreamSupport.stream(iter.spliterator(), true)
                .map(Order::toString)
                .collect(Collectors.joining(" , "));
    }

    @GetMapping("/{id}")
    public @ResponseBody String getOrder(@PathVariable UUID id) {
        Order order = orderRepository.findById(id.toString()).get();
        String response = "No orders found for this tenant!";
        if(order != null){
            logger.info("user: "+ order.getLastName());
            response = "first name: "+order.getOrderDetail() +", lastName: "+order.getLastName()+ ", id: "+order.getId();
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteOrder(@PathVariable UUID id) {
        String tenantId = TenantStorage.getCurrentTenant();
        Order order = orderRepository.findById(id.toString()).get();
        if(order != null){
            orderRepository.deleteById(id.toString(), new PartitionKey(order.getLastName()));
            return "Deleted " + id;
        }
        else{
            return "No orders found for this tenant!";
        }
    }
}
