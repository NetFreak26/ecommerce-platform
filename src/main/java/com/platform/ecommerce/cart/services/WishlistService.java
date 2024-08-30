package com.platform.ecommerce.cart.services;

import com.platform.ecommerce.cart.payloads.WishlistResponse;

public interface WishlistService {
    void addWishlistItem(Long productId);

    WishlistResponse getUserWishlist();

    WishlistResponse deleteItem(Long itemId);
}
