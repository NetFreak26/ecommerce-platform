package com.platform.ecommerce.cart.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartResponse {
    private List<ShoppingCartItemResponse> shoppingCartItems;
}
