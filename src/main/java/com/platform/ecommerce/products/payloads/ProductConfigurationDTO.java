package com.platform.ecommerce.products.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigurationDTO {
    private String name;
    private String value;
}
