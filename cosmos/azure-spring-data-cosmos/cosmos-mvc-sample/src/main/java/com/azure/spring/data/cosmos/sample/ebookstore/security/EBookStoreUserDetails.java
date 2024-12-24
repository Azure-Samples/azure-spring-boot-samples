// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.security;

import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.Address;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.CreditCard;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class EBookStoreUserDetails implements UserDetails {
    private Customer customer;

    public EBookStoreUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.customer.getPassword();
    }

    @Override
    public String getUsername() {
        return this.customer.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstName() {
        return this.customer
                .getName()
                .getFirstName();
    }

    public String getLastName() {
        return this.customer
                .getName()
                .getLastName();
    }

    public Address getAddress() {
        return this.customer.getAddress();
    }

    public void setAddress(Address address){
        this.customer.setAddress(address);
    }

    public String getCustomerId(){
        return this.customer.getId();
    }

    public  void setCreditCardNumber(CreditCard creditCard){this.customer.setCreditCard(creditCard); }

    public CreditCard getCreditCard(){return this.customer.getCreditCard();}
}
