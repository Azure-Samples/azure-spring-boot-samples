// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.security;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.Customer;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(CustomerDetailService.class);
    private CustomerRepository customerRepository;

    public CustomerDetailService(CustomerRepository userRepository) {
        this.customerRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer user = null;
        try {
            user = customerRepository
                    .findById(email, new PartitionKey(email))
                    .orElseThrow(() -> {
                        return new UsernameNotFoundException(email);
                    });
        } catch(UsernameNotFoundException e) {
            logger.error(e.toString());
            throw e;
        }
        return new EBookStoreUserDetails(user);
    }
}
