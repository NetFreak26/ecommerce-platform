package com.platform.ecommerce.cart.models;

import com.platform.ecommerce.users.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "shoppingCart", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

    public synchronized void addShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        this.shoppingCartItems.add(shoppingCartItem);
    }

    public synchronized void removeShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        this.shoppingCartItems.remove(shoppingCartItem);
    }
}
