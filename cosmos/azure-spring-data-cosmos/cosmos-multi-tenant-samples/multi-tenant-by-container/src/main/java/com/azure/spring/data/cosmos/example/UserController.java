package com.azure.spring.data.cosmos.example;
import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.example.tenant.TenantStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@PropertySource("classpath:application.yaml")
@Controller
@RequestMapping(path = "/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
        //get users with custom query based on "type" since entities are colocated
        Iterable<User> iter = userRepository.getAllUsers();
        return StreamSupport.stream(iter.spliterator(), true)
                .map(User::toString)
                .collect(Collectors.joining(" , "));
    }

    @GetMapping("/{id}")
    public @ResponseBody String getUser(@PathVariable UUID id) {
        User user = userRepository.findById(id.toString()).get();
        String response = "no users found for this tenant!";
        if(user != null){
            logger.info("user: "+ user.getLastName());
            response = "first name: "+user.getFirstName() +", lastName: "+user.getLastName()+ ", id: "+user.getId();
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteUser(@PathVariable UUID id) {
        String tenantId = TenantStorage.getCurrentTenant();
        User user = userRepository.findById(id.toString()).get();
        if(user != null){
            userRepository.deleteById(id.toString(), new PartitionKey(user.getLastName()));
            return "Deleted " + id;
        }
        else{
            return "no users found for this tenant!";
        }
    }
}
