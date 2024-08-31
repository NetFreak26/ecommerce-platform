package com.platform.ecommerce.orders.payloads;

import com.platform.ecommerce.products.payloads.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Integer quantity;
    private Double price;
    private ProductResponse product;
    private ReviewDTO review;
}
