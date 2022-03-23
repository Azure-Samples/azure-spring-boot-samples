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
package org.springframework.samples.petclinic.visits.web;

import com.google.common.collect.Lists;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Timed("petclinic.visit")
class VisitResource {

  private final VisitRepository visitRepository;

  @PostMapping("owners/*/pets/{petId}/visits")
  @ResponseStatus(HttpStatus.CREATED)
  Visit create(@Valid @RequestBody Visit visit, @PathVariable("petId") String petId) {

    visit.setPetId(petId);
    log.info("Saving visit {}", visit);
    return visitRepository.save(visit);
  }

  @GetMapping("owners/*/pets/{petId}/visits")
  Optional<Visit> visits(@PathVariable("petId") String petId) {
    return visitRepository.findById(petId);
  }

  @GetMapping("pets/visits")
  Visits visitsMultiGet(@RequestParam("petId") Set<String> petIds) {
    List<Visit> result = new ArrayList<>();
    for (String petId : petIds) {
      List<Visit> visitIterable = visitRepository.findByPetId(petId);
      result.addAll(visitIterable);
    }
    return new Visits(Lists.newArrayList(result));
  }

  @Value
  static class Visits {
    List<Visit> items;

    Visits(List<Visit> items) {
      this.items = items;
    }
  }
}
