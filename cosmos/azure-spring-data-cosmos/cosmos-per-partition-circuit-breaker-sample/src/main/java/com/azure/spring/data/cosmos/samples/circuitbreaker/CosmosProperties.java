// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.circuitbreaker;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "spring.data.cosmos")
public class CosmosProperties {
    private String uri;
    private String key;
    private String databaseName;
    private List<String> preferredRegions = new ArrayList<>();
    private int readFailureThreshold;
    private int writeFailureThreshold;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<String> getPreferredRegions() {
        return preferredRegions;
    }

    public void setPreferredRegions(List<String> preferredRegions) {
        this.preferredRegions = preferredRegions;
    }

    public int getReadFailureThreshold() {
        return readFailureThreshold;
    }

    public void setReadFailureThreshold(int readFailureThreshold) {
        this.readFailureThreshold = readFailureThreshold;
    }

    public int getWriteFailureThreshold() {
        return writeFailureThreshold;
    }

    public void setWriteFailureThreshold(int writeFailureThreshold) {
        this.writeFailureThreshold = writeFailureThreshold;
    }
}
