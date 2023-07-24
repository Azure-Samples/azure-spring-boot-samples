// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.controller.order;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.sample.ebookstore.model.cart.Cart;
import com.azure.spring.data.cosmos.sample.ebookstore.model.cart.CartService;
import com.azure.spring.data.cosmos.sample.ebookstore.model.order.OrderRepository;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.Address;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.CreditCard;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.Customer;
import com.azure.spring.data.cosmos.sample.ebookstore.model.customer.CustomerRepository;
import com.azure.spring.data.cosmos.sample.ebookstore.security.EBookStoreUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class OrderController {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CustomerRepository customerRepository;
    private final OrderHelper orderHelper;

    public OrderController(OrderRepository orderRepository, CartService cartService,CustomerRepository customerRepository, OrderHelper orderHelper) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.customerRepository=customerRepository;
        this.orderHelper = orderHelper;
    }

    @PostMapping(value = "/ebooks/order/create")
    public String createOrder(OrderForm orderForm, Model model, HttpSession session, @AuthenticationPrincipal EBookStoreUserDetails securedUser) {
        orderRepository.save(orderHelper.createOrder(orderForm));
        if (orderForm.getStreetAddress() != null) {
            Customer user = getCustomerUsingOrderFormDetails(orderForm);
            customerRepository.save(user);
            securedUser.setAddress(user.getAddress());
            securedUser.setCreditCardNumber(user.getCreditCard());
        }
        cartService.deleteCart(session.getId(), session.getId());
        model.addAttribute("customer", securedUser);
        return "orderconfirmation";
    }

    @PostMapping(value = "/ebooks/order/checkout")
    public String checkOut(@ModelAttribute Cart cart, Model model, HttpSession session, @AuthenticationPrincipal EBookStoreUserDetails securedUser) {
        model.addAttribute("customer", securedUser);
        model.addAttribute("order", orderHelper.createOrder(cart, securedUser.getUsername()));
        model.addAttribute("cartItemCount", cartService.getNumberOfItemsInTheCart(session.getId()));
        return "checkout";
    }


    @GetMapping(value = "/ebooks/order/customer/{customerId}")
    public String getCustomerOrders(@PathVariable String customerId, Model model, HttpSession session, @AuthenticationPrincipal EBookStoreUserDetails securedUser) {
        model.addAttribute("customer", securedUser);
        model.addAttribute("orders", orderRepository.getOrdersByCustomerIdOrderByTimestamp(customerId));
        model.addAttribute("cartItemCount", cartService.getNumberOfItemsInTheCart(session.getId()));
        return "orders";
    }

    Customer getCustomerUsingOrderFormDetails(OrderForm orderForm) {
        Customer customer = customerRepository
                .findById(orderForm.getCustomerId(), new PartitionKey(orderForm.getCustomerId()))
                .get();
        customer.setId(orderForm.getCustomerId());
        Address address = new Address(orderForm.getStreetAddress(), orderForm.getCity(), orderForm.getState(), orderForm.getZip(), "USA");
        CreditCard creditCard = new CreditCard(orderForm.getCreditCardNumber(),"Master","12/2025","123");
        customer.setAddress(address);
        customer.setCreditCard(creditCard);
        return customer;
    }

}
