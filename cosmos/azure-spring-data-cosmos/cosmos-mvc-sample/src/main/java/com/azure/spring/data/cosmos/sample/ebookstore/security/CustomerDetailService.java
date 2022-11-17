// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.spring.cosmos.ebookstore.security;

import com.azure.cosmos.models.PartitionKey;
import com.spring.cosmos.ebookstore.model.user.Customer;
import com.spring.cosmos.ebookstore.model.user.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CustomerRepository customerRepository;

    @Autowired
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
        return new SecuredCustomer(user);
    }
}
