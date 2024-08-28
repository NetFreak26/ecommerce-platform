package com.platform.ecommerce.products.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configurations", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "value"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String value;
}
