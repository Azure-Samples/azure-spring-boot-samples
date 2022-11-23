// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.controller.cart;

import com.azure.spring.data.cosmos.sample.ebookstore.model.cart.Cart;
import com.azure.spring.data.cosmos.sample.ebookstore.model.cart.CartItem;
import com.azure.spring.data.cosmos.sample.ebookstore.model.cart.CartService;
import com.azure.spring.data.cosmos.sample.ebookstore.security.EBookStoreUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = "/ebooks/cart/item/add")
    public String addItem(CartItem item, HttpSession session) {
        item.setId(UUID.randomUUID().toString());
        item.setQuantity(1);
        logger.info("adding item to cart");
        cartService.addItemToCart(session.getId(), item);
        return "redirect:/ebooks/index";
    }

    @GetMapping(value = "/ebooks/cart")
    public String getCart(Model model, HttpSession session, @AuthenticationPrincipal EBookStoreUserDetails securedUser) {
        Cart cart = cartService.getCart(session.getId());
        model.addAttribute("customer", securedUser);
        model.addAttribute("cart", cart);
        model.addAttribute("cartItemCount", cart.getItems().size());
        return "cart";

    }

    @GetMapping(value = "/ebooks/cart/delete/item/{id}")
    public String deleteItem(@PathVariable String id, HttpSession session) {
        logger.info("deleting cart item with id" + id);
        cartService.removeItemFromCart(session.getId(), id);
        return "redirect:/ebooks/cart";
    }

}
