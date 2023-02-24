package com.azure.spring.data.cosmos.example;

import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedFlux;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.query.CosmosQuery;
import com.azure.spring.data.cosmos.core.query.Criteria;
import com.azure.spring.data.cosmos.core.query.CriteriaType;
import com.azure.spring.data.cosmos.example.tenant.TenantStorage;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@PropertySource("classpath:application.yaml")
@Controller
@RequestMapping(path = "/all")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final TenantStorage tenantStorage;

    private final Environment env;
    private final CosmosTemplate cosmosTemplate;
    public HomeController(TenantStorage tenantStorage, Environment env, CosmosTemplate cosmosTemplate) {
        this.tenantStorage = tenantStorage;
        this.cosmosTemplate = cosmosTemplate;
        this.env = env;
    }

    @GetMapping
    public @ResponseBody String getAllTenantUsersAndOrders() {
        String tenantId = TenantStorage.getCurrentTenant();
        //retrieve string container name from list  (if it does not exist yet, create it).
        String tenantContainer = tenantStorage.getTenant(tenantId);
        //because order and user types are colocated with same partition key, we can hit the container once for both
        logger.info("database: "+env.getProperty("spring.data.cosmos.databaseName"));
        logger.info("container: "+tenantContainer);
        CosmosPagedFlux<JsonNode> pagedFlux = tenantStorage.client.getDatabase(env.getProperty("spring.data.cosmos.databaseName")).getContainer(tenantContainer).queryItems("SELECT * FROM c", JsonNode.class);
        List<JsonNode> json = pagedFlux.byPage(100).flatMap(pagedFluxResponse -> {
            return Flux.just(pagedFluxResponse
                    .getResults()
                    .stream()
                    .collect(Collectors.toList()));
        }).onErrorResume((exception) -> {
            logger.error(
                    "Exception. e: {}",
                    exception.getLocalizedMessage(),
                    exception);
            return Mono.empty();
        }).blockLast();

        return json.toString();
    }

}
