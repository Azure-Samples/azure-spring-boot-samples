// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.cosmos;

public class CosmosProperties {
    private String uri;

    private String key;

    private String secondaryKey;

    private String storeDatabase;

    private String securityDatabase;

    private boolean queryMetricsEnabled;

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

    public String getStoreDatabase() {
        return storeDatabase;
    }

    public void setStoreDatabase(String storeDatabase) {
        this.storeDatabase = storeDatabase;
    }

    public boolean isQueryMetricsEnabled() {
        return queryMetricsEnabled;
    }

    public void setQueryMetricsEnabled(boolean enableQueryMetrics) {
        this.queryMetricsEnabled = enableQueryMetrics;
    }

    public String getSecurityDatabase() {
        return securityDatabase;
    }

    public void setSecurityDatabase(String securityDatabase) {
        this.securityDatabase = securityDatabase;
    }

}
