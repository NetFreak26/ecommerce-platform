package com.platform.ecommerce.products.models;

import com.platform.ecommerce.categories.models.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String description;
    @Min(0)
    private Integer quantity;
    @Min(0)
    private Double originalPrice;
    @Min(0)
    private Double sellingPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "products_configurations",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_configuration_id")
    )
    private List<ProductConfiguration> productConfigurations = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductImage> productImages = new ArrayList<>();

    public void setProductConfigurations(List<ProductConfiguration> productConfigurations) {
        this.productConfigurations.clear();
        this.productConfigurations.addAll(productConfigurations);
    }

    public synchronized void addProductImage(ProductImage productImage) {
        if (this.productImages.contains(productImage)) {
            return;
        }
        this.productImages.add(productImage);
    }

    public synchronized void removeProductImage(ProductImage productImage) {
        this.productImages.remove(productImage);
    }
}
