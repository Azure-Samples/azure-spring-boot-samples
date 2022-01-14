package org.springframework.samples.petclinic.vets.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cloud.azure.cosmos")
class CosmosProperties {

  private String endpoint;

  private String key;

  private String database;

  private boolean populateQueryMetrics;
}
