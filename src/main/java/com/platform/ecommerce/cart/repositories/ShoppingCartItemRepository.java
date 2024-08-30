package com.platform.ecommerce.cart.repositories;

import com.platform.ecommerce.cart.models.ShoppingCart;
import com.platform.ecommerce.cart.models.ShoppingCartItem;
import com.platform.ecommerce.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    Optional<ShoppingCartItem> findByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);

    Optional<ShoppingCartItem> findByItemIdAndShoppingCart(Long itemId, ShoppingCart shoppingCart);
}
