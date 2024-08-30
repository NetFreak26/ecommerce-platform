package com.platform.ecommerce.cart.repositories;

import com.platform.ecommerce.cart.models.Wishlist;
import com.platform.ecommerce.cart.models.WishlistItem;
import com.platform.ecommerce.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    Optional<WishlistItem> findByWishlistAndProduct(Wishlist wishlist, Product product);

    Optional<WishlistItem> findByItemIdAndWishlist(Long itemId, Wishlist wishlist);
}
