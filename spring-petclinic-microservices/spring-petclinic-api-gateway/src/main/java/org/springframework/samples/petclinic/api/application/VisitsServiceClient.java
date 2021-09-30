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
package org.springframework.samples.petclinic.api.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.api.dto.Visits;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.joining;

/** @author Maciej Szarlinski */
@Component
@RequiredArgsConstructor
public class VisitsServiceClient {

  private String hostname = "http://visits-service/";

  private final WebClient.Builder webClientBuilder;

  public Mono<Visits> getVisitsForPets(final List<String> petIds) {
    return webClientBuilder
        .build()
        .get()
        .uri(hostname + "pets/visits?petId={petId}", joinIds(petIds))
        .retrieve()
        .bodyToMono(Visits.class);
  }

  private String joinIds(List<String> petIds) {
    return petIds.stream().map(Object::toString).collect(joining(","));
  }

  void setHostname(String hostname) {
    this.hostname = hostname;
  }
}
