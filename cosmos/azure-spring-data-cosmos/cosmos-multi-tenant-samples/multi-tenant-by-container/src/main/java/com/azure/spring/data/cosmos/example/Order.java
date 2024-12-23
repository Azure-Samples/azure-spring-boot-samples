// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.azure.spring.data.cosmos.example.tenant.TenantInterceptor;
import org.springframework.data.annotation.Id;

/**
 * Define the Order container. Set "autoCreateContainer" to false so that container name
 * can be created/referenced dynamically using tenant id from {@link TenantInterceptor}
 */
@Container(autoCreateContainer = false)
public class Order {
    @Id
    private String id;
    private String orderDetail;
    @PartitionKey
    private String lastName;
    private String type;
    public Order() {
    }
    public Order(String id, String orderDetail, String lastName, String type) {
        this.id = id;
        this.orderDetail = orderDetail;
        this.lastName = lastName;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("com.azure.spring.data.cosmos.example.Order: %s, %s, %s, %s", orderDetail, lastName, id, type);
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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
