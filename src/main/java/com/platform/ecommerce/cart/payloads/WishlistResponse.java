package com.platform.ecommerce.cart.payloads;

import com.platform.ecommerce.products.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {
    private List<WishlistItemResponse> wishlistItems;
}
