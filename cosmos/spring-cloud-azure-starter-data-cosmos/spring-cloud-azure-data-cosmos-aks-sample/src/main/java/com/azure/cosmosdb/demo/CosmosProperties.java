// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.cosmosdb.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "azure.cosmos")
public class CosmosProperties {

    private String uri;

    private String key;

    private String secondaryKey;

    private String database;

    private boolean queryMetricsEnabled;

    private boolean responseDiagnosticsEnabled;



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

    public void setDatabase(String database){
        this.database = database;
    }
    public String getDatabase() {
        return database;
    }

    public boolean isQueryMetricsEnabled() {
        return queryMetricsEnabled;
    }

    public void setQueryMetricsEnabled(boolean enableQueryMetrics) {
        this.queryMetricsEnabled = enableQueryMetrics;
    }

    public boolean isResponseDiagnosticsEnabled() {
        return responseDiagnosticsEnabled;
    }

    public void setResponseDiagnosticsEnabled(boolean enableResponseDiagnostics) {
        this.responseDiagnosticsEnabled = enableResponseDiagnostics;
    }
}