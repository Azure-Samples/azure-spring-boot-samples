// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example;
import com.azure.cosmos.models.PartitionKey;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public @ResponseBody String createUser(@RequestBody User user) {
        UUID uuid = UUID.randomUUID();
        user.setId(String.valueOf(uuid));
        user.setType("user");
        userRepository.save(user);
        return String.format("Added %s.", user);
    }

    @GetMapping
    public @ResponseBody String getAllUsers() {
        Iterable<User> iter = userRepository.getAllUsers();
        return StreamSupport.stream(iter.spliterator(), true)
                .map(User::toString)
                .collect(Collectors.joining(" , "));
    }

    @GetMapping("/{id}")
    public @ResponseBody String getUser(@PathVariable UUID id) {
        try {
            User user = userRepository.findById(id.toString()).get();
            return user.toString();
        }
        catch (NoSuchElementException e){
            return "No users found for this tenant!";
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteUser(@PathVariable UUID id) {
        try {
            User user = userRepository.findById(id.toString()).get();
            userRepository.deleteById(id.toString(), new PartitionKey(user.getLastName()));
            return "Deleted user:" + id;
        }
        catch (NoSuchElementException e){
            return "No users found for this tenant!";
        }
    }
}
