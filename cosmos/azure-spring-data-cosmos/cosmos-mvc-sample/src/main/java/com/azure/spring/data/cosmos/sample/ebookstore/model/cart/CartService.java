// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.model.cart;

public interface CartService {
    public void removeItemFromCart(String cartId, String itemId);
    public Integer getNumberOfItemsInTheCart(String id);
    public void deleteCart(String id, String partitionKey);
    public void addItemToCart(String cartId, CartItem item);
    public Cart getCart(String id);
}
