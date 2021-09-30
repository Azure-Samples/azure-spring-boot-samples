/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.customers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// For AAD Sample
// import
// org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Maciej Szarlinski
 */


// For AAD Sample
// @EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class CustomersServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomersServiceApplication.class, args);
  }
}
