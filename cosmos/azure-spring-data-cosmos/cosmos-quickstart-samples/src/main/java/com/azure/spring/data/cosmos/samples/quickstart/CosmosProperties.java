// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.quickstart;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "spring.data.cosmos")
public class CosmosProperties {

    private String uri;

    private String key;

    private String secondaryKey;

    private String databaseName;

    private boolean queryMetricsEnabled;

    private List<String> preferredRegions = new ArrayList<>();

    private Duration endToEndTimeout;

    private Duration availabilityThreshold;

    private Duration availabilityThresholdStep;

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

    public String getSecondaryKey() {
        return secondaryKey;
    }

    public void setSecondaryKey(String secondaryKey) {
        this.secondaryKey = secondaryKey;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public boolean isQueryMetricsEnabled() {
        return queryMetricsEnabled;
    }

    public void setQueryMetricsEnabled(boolean enableQueryMetrics) {
        this.queryMetricsEnabled = enableQueryMetrics;
    }

    public List<String> getPreferredRegions() {
        return preferredRegions;
    }

    public void setPreferredRegions(List<String> preferredRegions) {
        this.preferredRegions = preferredRegions;
    }

    public Duration getEndToEndTimeout() {
        return endToEndTimeout;
    }

    public void setEndToEndTimeout(Duration endToEndTimeout) {
        this.endToEndTimeout = endToEndTimeout;
    }

    public Duration getAvailabilityThreshold() {
        return availabilityThreshold;
    }

    public void setAvailabilityThreshold(Duration availabilityThreshold) {
        this.availabilityThreshold = availabilityThreshold;
    }

    public Duration getAvailabilityThresholdStep() {
        return availabilityThresholdStep;
    }

    public void setAvailabilityThresholdStep(Duration availabilityThresholdStep) {
        this.availabilityThresholdStep = availabilityThresholdStep;
    }
}
