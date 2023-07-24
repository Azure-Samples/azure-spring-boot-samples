// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.controller.customer;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.Customer;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.CustomerRepository;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(CustomerRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/ebooks/user/createAccount")
    public String createAccount(@ModelAttribute CustomerForm userForm, Model model) {
        String password = this.passwordEncoder.encode(userForm.getPassword());
        Customer user = new Customer(userForm.getEmail(), password, new Name(userForm.getFirstName(), userForm.getLastName()));
        if (userRepository.findById(userForm.getEmail(), new PartitionKey(userForm.getEmail())).isPresent()) {
            model.addAttribute("accountCreationFailed", userForm.getEmail() + "is already associated with an account. Try another email ID");
            return "create-account";
        }
        userRepository.save(user);
        return "login";
    }
}
