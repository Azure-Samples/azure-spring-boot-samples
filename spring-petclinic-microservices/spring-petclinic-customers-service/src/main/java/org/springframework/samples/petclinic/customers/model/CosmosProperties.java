package org.springframework.samples.petclinic.customers.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "azure.cosmosdb")
public class CosmosProperties {

    private String uri;

    private String key;

    private String secondaryKey;

    private String database;

    private boolean populateQueryMetrics;
}
