// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.featureexamples;

import com.azure.spring.data.cosmos.samples.common.User;
import com.azure.spring.data.cosmos.samples.quickstart.UserRepository;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.query.CosmosPageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ComponentScan(basePackages = "com.azure.spring.data.cosmos.samples.*")
public class Pagination implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(Pagination.class);
    private UserRepository userRepository;
    private CosmosTemplate cosmosTemplate;

    public Pagination(UserRepository userRepository, CosmosTemplate cosmosTemplate){
        this.userRepository = userRepository;
        this.cosmosTemplate = cosmosTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Pagination.class, args);
    }

    public void run(String... var1) {

        final User testUser1 = new User("testId1", "testFirstName1", "testLastName1");
        final User testUser2 = new User("testId2", "testFirstName2", "testLastName2");
        final User testUser3 = new User("testId3", "testFirstName3", "testLastName3");
        final User testUser4 = new User("testId4", "testFirstName4", "testLastName4");

        logger.info("Pagination test...");
        logger.info("Using sync repository");

        // <Delete>
        userRepository.deleteAll();
        // </Delete>

        // <Create>
        logger.info("Saving user : {}, {}, {}, {}", testUser1, testUser2, testUser3, testUser4);
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        userRepository.save(testUser4);
        // </Create>

        // <Pagination Query>
        // This is how you would perform a pagination query with userRepository.

        // Get page
        final PageRequest pageRequest = new CosmosPageRequest(0, 2, null);
        final Page<User> page = cosmosTemplate.findAll(pageRequest, User.class, "myContainer");
        logger.info("(1) Get users from cosmos DB. Users page = {}", page.getContent());

        //Get page 2 with continuation token
        final Page<User> pageWithContToken = cosmosTemplate.findAll(page.getPageable().next(), User.class, "myContainer");
        logger.info("(2) Get users from cosmos DB. Users page = {}", pageWithContToken.getContent());

        //Get page 2 without continuation token
        final PageRequest pageRequest2 = new CosmosPageRequest(1, 2, null);
        final Page<User> page2 = cosmosTemplate.findAll(pageRequest2, User.class, "myContainer");
        logger.info("(3) Get users from cosmos DB. Users page = {}", page2.getContent());

        //Get page with sort
        final Sort sort = Sort.by(Sort.Direction.ASC, "lastName");
        final PageRequest pageRequestWithSort = new CosmosPageRequest(0, 2, null, sort);
        final Page<User> pageWithSort = cosmosTemplate.findAll(pageRequestWithSort, User.class, "myContainer");
        logger.info("(4) Get users from cosmos DB. Users page = {}", pageWithSort.getContent());

        //Ge page with offset
        final PageRequest pageRequestWithOffset = CosmosPageRequest.of(1, 0, 2, null, Sort.by(Sort.Direction.ASC, "lastName"));
        final Page<User> pageWithOffset = cosmosTemplate.findAll(pageRequestWithOffset, User.class, "myContainer");
        logger.info("(5) Get users from cosmos DB. Users page = {}", pageWithOffset.getContent());
        // </Pagination Query>
    }
}
