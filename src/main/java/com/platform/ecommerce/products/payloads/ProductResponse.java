package com.platform.ecommerce.products.payloads;

import com.platform.ecommerce.categories.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long productId;

    private String productName;
    private String description;
    private Integer quantity;
    private Double originalPrice;
    private Double sellingPrice;

    private Category category;

    private List<ProductConfigurationDTO> productConfigurations;
    private List<ProductImageDTO> productImages;
}
