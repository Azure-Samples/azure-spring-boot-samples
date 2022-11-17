// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.cosmos.springexamples.featureexamples;

import com.azure.cosmos.springexamples.common.User;
import com.azure.cosmos.springexamples.quickstart.sync.UserRepository;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.query.CosmosPageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootApplication
@ComponentScan(basePackages = "com.azure.cosmos.springexamples.*")
public class Pagination implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(Pagination.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CosmosTemplate cosmosTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Pagination.class, args);
    }

    public void run(String... var1) {

        final User testUser1 = new User("testId1", "testFirstName", "testLastName1");
        final User testUser2 = new User("testId2", "testFirstName", "testLastName2");
        final User testUser3 = new User("testId3", "testFirstName", "testLastName3");
        final User testUser4 = new User("testId4", "testFirstName", "testLastName4");

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

        //Get page
        final PageRequest pageRequest = new CosmosPageRequest(0, 2, null);
        final Page<User> page = cosmosTemplate.findAll(pageRequest, User.class, "myContainer");
        logger.info("(1) Found user's (query): {}", page.getContent());

        //Get page 2 with continuation token
        final Page<User> pageWithContToken = cosmosTemplate.findAll(page.getPageable().next(), User.class, "myContainer");
        logger.info("(2) Found user's (query): {}", pageWithContToken.getContent());

        //Get page 2 without continuation token
        final PageRequest pageRequest2 = new CosmosPageRequest(1, 2, null);
        final Page<User> page2 = cosmosTemplate.findAll(pageRequest2, User.class, "myContainer");
        logger.info("(3) Found user's (query): {}", page2.getContent());

        //Get page with sort
        final Sort sort = Sort.by(Sort.Direction.ASC, "lastName");
        final PageRequest pageRequestWithSort = new CosmosPageRequest(0, 2, null, sort);
        final Page<User> pageWithSort = cosmosTemplate.findAll(pageRequestWithSort, User.class, "myContainer");
        logger.info("(4) Found user's (query): {}", pageWithSort.getContent());

        //Ge page with offset
        final PageRequest pageRequestWithOffset = CosmosPageRequest.of(1, 0, 2, null, Sort.by(Sort.Direction.ASC, "lastName"));
        final Page<User> pageWithOffset = cosmosTemplate.findAll(pageRequestWithOffset, User.class, "myContainer");
        logger.info("(5) Found user's (query): {}", pageWithOffset.getContent());

        // </Pagination Query>
    }
}
