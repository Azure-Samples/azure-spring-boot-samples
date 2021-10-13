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
package org.springframework.samples.petclinic.customers.web;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
// Required for AAD starter to work. For sample only
// import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
// For AAD sample
// @PreAuthorize("hasRole('ROLE_owners')")
@RequestMapping("/owners")
@RestController
@Timed("petclinic.owner")
@RequiredArgsConstructor
@Slf4j
class OwnerResource {

  private final OwnerRepository ownerRepository;

  /**
   *
   * Create Owner
   * @return created owner
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Owner createOwner(@Valid @RequestBody Owner owner) {
    return ownerRepository.save(owner);
  }

  /**
   * Read single Owner
   *
   * @return findowner
   */
  @GetMapping(value = "/{ownerId}")
  public Optional<Owner> findOwner(@PathVariable("ownerId") String ownerId) {
    return ownerRepository.findById(ownerId);
  }

    /**
     * Read List of Owners
     */
  @GetMapping
  @Cacheable("owners")
  public List<Owner> findAll() {
    List<Owner> list = new ArrayList<>();
    ownerRepository.findAll().forEach(list::add);
    return list;
  }

    /**
     * Clears the cache for all Owners
     */
  @GetMapping(value = "/clearcache")
  @CacheEvict("owners")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void clearAllCache() {}

  /**
   * Read List of Owners
   */
  @GetMapping(value = "/{ownerId}/pets")
  public List<Pet> findAllPets(@PathVariable("ownerId") String ownerId) {
    Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
    List<Pet> list = new ArrayList<>();
    if (optionalOwner.isPresent()) {
      list = optionalOwner.get().getPets();
    }
    return list;
  }

    /**
     * Update Owner
     */
  @PutMapping(value = "/{ownerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOwner(@PathVariable("ownerId") String ownerId, @Valid @RequestBody Owner ownerRequest) {
    Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
        ownerOptional.ifPresent(owner -> {
          owner.setFirstName(ownerRequest.getFirstName());
          owner.setLastName(ownerRequest.getLastName());
          owner.setCity(ownerRequest.getCity());
          owner.setAddress(ownerRequest.getAddress());
          owner.setTelephone(ownerRequest.getTelephone());
          log.info("Saving owner {}", owner);
          ownerRepository.save(owner);
        });
  }
}
