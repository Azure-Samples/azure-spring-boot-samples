// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

// Container names will be created/referenced dynamically using tenant id from TenantInterceptor,
@Container(autoCreateContainer = true)
public class Order {
    @Id
    private String id;
    private String orderDetail;
    @PartitionKey
    private String lastName;
    public Order() {
    }
    public Order(String id, String orderDetail, String lastName) {
        this.id = id;
        this.orderDetail = orderDetail;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("com.azure.spring.data.cosmos.Order: %s %s, %s", orderDetail, lastName, id);
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
