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
package org.springframework.samples.petclinic.customers.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import lombok.*;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Maciej Szarlinski
 */
@Container(containerName = "pets")
@Builder(builderMethodName = "pet")
@AllArgsConstructor
@NoArgsConstructor
public class Pet implements Serializable {

    @Id
    @GeneratedValue
    private String id;

  private String name;

  private Date birthDate;

  private String petType;

  //    @JsonIgnore
  //    private Owner owner;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(final Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getType() {
    return petType;
  }

  public void setType(final String type) {
    this.petType = type;
  }

  //    public Owner getOwner() {
  //        return owner;
  //    }
  //
  //    public void setOwner(final Owner owner) {
  //        this.owner = owner;
  //    }

  @Override
  public String toString() {
    return new ToStringCreator(this)
        .append("id", this.getId())
        .append("name", this.getName())
        .append("birthDate", this.getBirthDate())
        .append("type", this.getType())
        // .append("ownerFirstname", this.getOwner().getFirstName())
        // .append("ownerLastname", this.getOwner().getLastName())
        .toString();
  }
}
