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
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.samples.petclinic.customers.model.PetRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Maciej Szarlinski
 */
@RestController
@Timed("petclinic.pet")
@RequiredArgsConstructor
@Slf4j
class PetResource {

  private final PetRepository petRepository;
  private final OwnerRepository ownerRepository;

  @GetMapping("/petTypes")
  public List<String> getPetTypes() {
    return petRepository.getPetTypes();
  }

  @PostMapping("/owners/{ownerId}/pets")
  @ResponseStatus(HttpStatus.CREATED)
  public Pet processCreationForm(
        @RequestBody PetRequest petRequest,
        @PathVariable("ownerId") String ownerId) {

    Pet pet = new Pet();
    Pet save = save(pet, petRequest);

    pet.setId(save.getId());
    pet.setName(save.getName());
    pet.setBirthDate(save.getBirthDate());
    pet.setType(save.getType());

    Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
    optionalOwner.ifPresent(owner -> owner.addPet(pet));
    ownerRepository.save(optionalOwner.get());

    return save;
  }

  @PutMapping("/owners/{ownerId}/pets/{petId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void processUpdateForm(
      @RequestBody PetRequest petRequest, @PathVariable("ownerId") String ownerId) {
    String petId = petRequest.getId();
    Optional<Pet> petOptional = findPetById(petId);
    petOptional.ifPresent(
        pet -> {
          Pet save = save(pet, petRequest);
          Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);

          optionalOwner.ifPresent(
              owner -> {
                List<Pet> pets = owner.getPets();
                for (Pet p : pets) {
                  if (p.getId().equals(save.getId())) {
                    p.setName(save.getName());
                    p.setBirthDate(save.getBirthDate());
                    p.setType(save.getType());
                  }
                }
              });
          ownerRepository.save(optionalOwner.get());
        });
  }

  private Pet save(final Pet pet, final PetRequest petRequest) {

    pet.setName(petRequest.getName());
    pet.setBirthDate(petRequest.getBirthDate());
    pet.setType(petRequest.getType());

    log.info("Saving pet {}", pet);
    return petRepository.save(pet);
  }

  @GetMapping("owners/*/pets/{petId}")
  public PetDetails findPet(@PathVariable("petId") String petId) {
    return new PetDetails(findPetById(petId));
  }

  private Optional<Pet> findPetById(String petId) {
    Optional<Pet> optionalPet = petRepository.findById(petId);

    if (!optionalPet.isPresent()) {
      throw new ResourceNotFoundException("Pet " + petId + " not found");
    }
    return optionalPet;
  }
}
