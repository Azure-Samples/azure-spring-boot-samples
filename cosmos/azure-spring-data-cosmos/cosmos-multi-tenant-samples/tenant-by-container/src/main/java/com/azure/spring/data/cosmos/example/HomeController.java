package com.azure.spring.data.cosmos.example;

import com.azure.core.http.rest.Page;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.query.CosmosPageRequest;
import com.azure.spring.data.cosmos.example.tenant.TenantStorage;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@PropertySource("classpath:application.yaml")
@Controller
@RequestMapping(path = "/all")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final TenantStorage tenantStorage;


    private final UserRepository userRepository;

    private final Environment env;
    private final CosmosTemplate cosmosTemplate;
    public HomeController(TenantStorage tenantStorage, UserRepository userRepository, Environment env, CosmosTemplate cosmosTemplate) {
        this.tenantStorage = tenantStorage;
        this.userRepository = userRepository;
        this.cosmosTemplate = cosmosTemplate;
        this.env = env;
    }

    @GetMapping
    public @ResponseBody String getAllTenantUsersAndOrders() {
        String tenantId = TenantStorage.getCurrentTenant();
        //retrieve string container name from list  (if it does not exist yet, create it).
        String tenantContainer = tenantStorage.getTenant(tenantId);

        logger.info("database: "+env.getProperty("spring.data.cosmos.databaseName"));
        logger.info("container: "+tenantContainer);

        //because order and user types are colocated with same partition key, we can hit the container once for both
        //and return the results in a single json object (can split into entities based on type later if needed)
        final PageRequest cosmosPageRequest = CosmosPageRequest.of(0, 10);
        List<JsonNode> json = userRepository.getOrdersAndUsers(cosmosPageRequest).toList();

        return json.toString();
    }

}
