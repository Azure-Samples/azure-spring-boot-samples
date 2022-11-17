// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.model.cart;

import com.azure.cosmos.models.PartitionKey;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DefaultCartService implements CartService {
    private CartRepository cartRepository;

    public DefaultCartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void removeItemFromCart(String cartId, String itemId) {
        Cart cart = cartRepository.findById(cartId, new PartitionKey(cartId)).get();
        List<CartItem> items = cart.getItems();
        if (items.size() == 1) {
            deleteCart(cartId, cartId);
            return;
        }
        cart.setSubTotal(cart
                .getSubTotal()
                .subtract(getPrice(itemId, cart)));
        items.removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    private BigDecimal getPrice(String itemId, Cart cart) {
        return cart
                .getItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .get()
                .getPrice();
    }

    @Override
    public Integer getNumberOfItemsInTheCart(String cartId) {
        return cartRepository.findById(cartId, new PartitionKey(cartId)).map(cart -> cart.getItems().size()).orElse(0);
    }

    @Override
    public void deleteCart(String id, String partitionKey) {
        cartRepository.deleteById(id, new PartitionKey(partitionKey));
    }

    @Override
    public void addItemToCart(String cartId, CartItem item) {
        Cart cart = cartRepository.findById(cartId, new PartitionKey(cartId)).orElseGet(Cart::new);
        if (cart.getId() == null) {
            cart.setId(cartId);
            cart.setSubTotal(item.getPrice());
            cart.getItems().add(item);
            cartRepository.save(cart);
            return;
        }
        cart.getItems().add(item);
        cart.setSubTotal(cart
                .getItems()
                .stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        cartRepository.save(cart);
    }

    @Override
    public Cart getCart(String id) {
        return cartRepository.findById(id, new PartitionKey(id)).orElseGet(Cart::new);
    }
}
