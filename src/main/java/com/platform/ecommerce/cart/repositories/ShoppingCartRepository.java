package com.platform.ecommerce.cart.repositories;

import com.platform.ecommerce.cart.models.ShoppingCart;
import com.platform.ecommerce.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    ShoppingCart findByUser(User user);
}
