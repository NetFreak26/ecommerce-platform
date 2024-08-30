package com.platform.ecommerce.cart.services;

import com.platform.ecommerce.cart.payloads.ShoppingCartItemDTO;
import com.platform.ecommerce.cart.payloads.ShoppingCartResponse;

public interface ShoppingCartService {

    void addSoppingCartItem(ShoppingCartItemDTO shoppingCartItemDTO);

    ShoppingCartResponse getUserCart();

    ShoppingCartResponse updateShoppingCartItem(Long itemId, Integer quantity);

    ShoppingCartResponse deleteShoppingCartItem(Long itemId);
}
