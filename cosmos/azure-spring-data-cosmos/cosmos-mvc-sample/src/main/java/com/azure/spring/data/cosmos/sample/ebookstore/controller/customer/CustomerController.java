// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.spring.cosmos.ebookstore.controller.customer;

import com.azure.cosmos.models.PartitionKey;
import com.spring.cosmos.ebookstore.model.user.Name;
import com.spring.cosmos.ebookstore.model.user.Customer;
import com.spring.cosmos.ebookstore.model.user.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CustomerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerController(CustomerRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/ebooks/user/createAccount")
    public String createAccount(@ModelAttribute CustomerForm userForm, Model model) {
       String password = this.passwordEncoder.encode(userForm.getPassword());
      Customer user = new Customer(userForm.getEmail(),password, new Name(userForm.getFirstName(),userForm.getLastName()));
      if(userRepository.findById(userForm.getEmail(), new PartitionKey(userForm.getEmail())).isPresent()){
          logger.info(userForm.getEmail()+ "is already associated with an account");
          model.addAttribute("accountCreationFailed", "Try another email ID");
          return "createaccount";
      }
      userRepository.save(user);
      return "login";
    }
}
