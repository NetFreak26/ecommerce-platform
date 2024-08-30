package com.platform.ecommerce.cart.payloads;

import com.platform.ecommerce.cart.models.ShoppingCartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartResponse {
    private List<ShoppingCartItem> shoppingCartItems;
}
