// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.controller.home;

import com.azure.spring.data.cosmos.sample.ebookstore.model.book.BookRepositoryAsync;
import com.azure.spring.data.cosmos.sample.ebookstore.model.book.Response;
import com.azure.spring.data.cosmos.sample.ebookstore.model.cart.CartService;
import com.azure.spring.data.cosmos.sample.ebookstore.security.EBookStoreUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final BookRepositoryAsync bookRepository;
    private final CartService cartService;

    public static final int REQUEST_PREFERRED_PAGE_SIZE = 18;
    public static final int REQUEST_PAGES_TO_RETURN = 1;
    public static final int RESPONSE_PREFERRED_PAGE_SIZE = 6;
    public static final int RESPONSE_PAGES_TO_RETURN = 1;

    public HomeController(BookRepositoryAsync bookRepository, CartService cartService) {
        this.bookRepository = bookRepository;
        this.cartService = cartService;
    }


    @RequestMapping(value = "/ebooks/index", method = { RequestMethod.GET, RequestMethod.POST })
    public String home(Model model, HttpSession session, @AuthenticationPrincipal EBookStoreUserDetails securedUser, @RequestParam(required=false, name="category_name") String category) {
        model.addAttribute("response", bookRepository.getBooks(REQUEST_PREFERRED_PAGE_SIZE, REQUEST_PAGES_TO_RETURN, category));
        model.addAttribute("cartItemCount", cartService.getNumberOfItemsInTheCart(session.getId()));
        model.addAttribute("customer", securedUser);
        model.addAttribute("category", category);
        return "index";
    }

    @PostMapping(value = "/ebooks/next")
    @ResponseBody
    public Response next(@RequestBody String continuationToken, @RequestParam("category_name") String category) {
        Response result=bookRepository.getBooks(continuationToken,RESPONSE_PREFERRED_PAGE_SIZE,RESPONSE_PAGES_TO_RETURN);

        return result;
    }

    @GetMapping(value = "/ebooks/login")
    public static final String login(Model model, @AuthenticationPrincipal EBookStoreUserDetails securedUser, @RequestParam(required=false, name="category_name") String category) {
        if(securedUser == null) {
            return "login";
        }
        else{
            return "redirect:index?category_name=" + category;
        }    
    }

    @GetMapping(value = "/ebooks/loginError")
    public static final String loginError(Model model) {
        logger.info("Login failed");
        model.addAttribute("loginFailed", "Email ID or Password is invalid");
        return "login";
    }

    @GetMapping(value = "/ebooks/createAccount")
    public static final String createAccount(Model model) {
        return "create-account";
    }
}
