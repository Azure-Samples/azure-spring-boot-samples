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
package org.springframework.samples.petclinic.visits.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.GenerationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 * @author Maciej Szarlinski
 */

@Container(containerName = "visits")
@Builder(builderMethodName = "visit")
@AllArgsConstructor
@NoArgsConstructor
public class Visit {

    @Id
    @GeneratedValue
    private String id;

    private String petId;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visit_date = new Date();

    @Size(max = 8192)
    private String description;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return visit_date;
    }

    public String getDescription() {
        return description;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(final String petId) {
        this.petId = petId;
    }

}
