package com.platform.ecommerce.cart.models;

import com.platform.ecommerce.users.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "wishlist", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    public synchronized void addWishlistItem(WishlistItem wishlistItem) {
        this.wishlistItems.add(wishlistItem);
    }

    public synchronized void removeWishlistItem(WishlistItem wishlistItem) {
        this.wishlistItems.remove(wishlistItem);
    }
}
