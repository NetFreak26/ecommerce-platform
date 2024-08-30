package com.platform.ecommerce.cart.payloads;

import com.platform.ecommerce.products.payloads.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItemResponse {
    private ProductResponse product;
    private Integer quantity;
}
