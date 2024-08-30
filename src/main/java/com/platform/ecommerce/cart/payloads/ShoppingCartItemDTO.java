package com.platform.ecommerce.cart.payloads;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItemDTO {
    @NonNull
    private Long productId;
    @NonNull
    @Min(1)
    private Integer quantity;
}
