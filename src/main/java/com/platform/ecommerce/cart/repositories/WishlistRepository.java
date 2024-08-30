package com.platform.ecommerce.cart.repositories;

import com.platform.ecommerce.cart.models.Wishlist;
import com.platform.ecommerce.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByUser(User user);
}
