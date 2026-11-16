// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.availability;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

@Container(containerName = "query-availability-items", autoCreateContainer = false)
public class SampleItem {
    @Id
    private String id;

    @PartitionKey
    private String partitionKey;

    public SampleItem() {
    }

    public SampleItem(String id, String partitionKey) {
        this.id = id;
        this.partitionKey = partitionKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }
}
