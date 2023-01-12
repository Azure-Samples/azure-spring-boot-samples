// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.model.customer;

import org.apache.commons.lang3.StringUtils;

public class Name {
    private String firstName;
    private String lastName;
    public Name() {
    }
    public Name(String firstName, String lastName) {
        this.firstName = StringUtils.capitalize(firstName);
        this.lastName = StringUtils.capitalize(lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
