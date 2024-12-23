// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.model.customer;

import org.springframework.util.StringUtils;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;
    private String Country;
    public Address(String street, String city, String state, String zip, String country) {
        this.street = street;
        this.city = StringUtils.capitalize(city);
        this.state = StringUtils.capitalize(state);
        this.zip = zip;
        Country = StringUtils.capitalize(country);
    }
    public String getStreet() {
        return street;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZip() {
        return zip;
    }
    public String getCountry() {
        return Country;
    }
}
