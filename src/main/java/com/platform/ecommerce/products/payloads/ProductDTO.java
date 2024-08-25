package com.platform.ecommerce.products.payloads;

import com.platform.ecommerce.categories.models.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotBlank
    private String productName;

    private String description;

    @Min(0)
    private Integer quantity;
    @NotNull
    private Double originalPrice;
    @NotNull
    private Double sellingPrice;

    @NotNull
    private List<ProductConfigurationDTO> productConfigurations;
}
