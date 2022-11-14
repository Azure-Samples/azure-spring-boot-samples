// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.spring.cosmos.ebookstore.controller.order;

import com.azure.cosmos.models.PartitionKey;
import com.spring.cosmos.ebookstore.model.cart.CartService;
import com.spring.cosmos.ebookstore.model.cart.Cart;
import com.spring.cosmos.ebookstore.model.order.OrderRepository;
import com.spring.cosmos.ebookstore.model.user.Address;
import com.spring.cosmos.ebookstore.model.user.CreditCard;
import com.spring.cosmos.ebookstore.model.user.Customer;
import com.spring.cosmos.ebookstore.model.user.CustomerRepository;
import com.spring.cosmos.ebookstore.security.SecuredCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class OrderController {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CustomerRepository customerRepository;
    private final OrderHelper orderHelper;

    @Autowired
    public OrderController(OrderRepository orderRepository, CartService cartService,CustomerRepository customerRepository, OrderHelper orderHelper) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.customerRepository=customerRepository;
        this.orderHelper = orderHelper;
    }

    @PostMapping(value = "/ebooks/order/create")
    public String createOrder(OrderForm orderForm, Model model, HttpSession session, @AuthenticationPrincipal SecuredCustomer securedUser) {

        orderRepository.save(orderHelper.getOrderFromOrderForm(orderForm));
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
    public String checkOut(@ModelAttribute Cart cart, Model model, HttpSession session, @AuthenticationPrincipal SecuredCustomer securedUser) {
        model.addAttribute("customer", securedUser);
        model.addAttribute("order", orderHelper.getOrder(cart, securedUser.getUsername()));
        model.addAttribute("cartItemCount", cartService.getNumberOfItemsInTheCart(session.getId()));
        return "checkout";
    }


    @GetMapping(value = "/ebooks/order/customer/{customerId}")
    public String getCustomerOrders(@PathVariable String customerId, Model model, HttpSession session, @AuthenticationPrincipal SecuredCustomer securedUser) {
        model.addAttribute("customer", securedUser);
        model.addAttribute("orders", orderRepository.getOrdersByCustomerId(customerId));
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
