package com.azure.spring.data.cosmos.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@PropertySource("classpath:application.yaml")
@Controller
@RequestMapping(path = "/all")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public HomeController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public @ResponseBody String getAllTenantUsersAndOrders() {
        Iterable<Order> iterOrder = orderRepository.findAll();
        Iterable<User> iterUser = userRepository.findAll();
        String orders = StreamSupport.stream(iterOrder.spliterator(), true)
                .map(Order::toString)
                .collect(Collectors.joining(" , "));
        String users = StreamSupport.stream(iterUser.spliterator(), true)
                .map(User::toString)
                .collect(Collectors.joining(" , "));
        return orders + users;
    }
}
