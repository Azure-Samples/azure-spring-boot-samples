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
package org.springframework.samples.petclinic.vets.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 * @author Maciej Szarlinski
 */
@Container(containerName = "vets")
@Builder(builderMethodName = "vet")
@AllArgsConstructor
@NoArgsConstructor
public class Vet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

  private Set<Specialty> specialties;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  protected Set<Specialty> getSpecialtiesInternal() {
    if (this.specialties == null) {
      this.specialties = new HashSet<>();
    }
    return this.specialties;
  }

  /**
   * getSpecialties
   * @return a list of Specialty
   */
  @XmlElement
  public List<Specialty> getSpecialties() {
    List<Specialty> sortedSpecs = new ArrayList<>(getSpecialtiesInternal());
    PropertyComparator.sort(sortedSpecs, new MutableSortDefinition("name", true, true));
    return Collections.unmodifiableList(sortedSpecs);
  }

  public int getNrOfSpecialties() {
    return getSpecialtiesInternal().size();
  }

  public void addSpecialty(Specialty specialty) {
    getSpecialtiesInternal().add(specialty);
  }
}
