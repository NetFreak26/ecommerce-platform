package com.platform.ecommerce.products.repositories;

import com.platform.ecommerce.products.models.ProductConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductConfigurationRepository extends JpaRepository<ProductConfiguration, Long> {
    Optional<ProductConfiguration> findByNameAndValue(String name, String value);
}
