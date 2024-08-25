package com.platform.ecommerce.products.services;

import com.platform.ecommerce.products.payloads.ProductDTO;
import com.platform.ecommerce.products.payloads.ProductPageResponse;
import com.platform.ecommerce.products.payloads.ProductResponse;

public interface IProductService {
    ProductResponse addProduct(ProductDTO productDTO, Long categoryId);
    ProductPageResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductPageResponse getAllProductsByCategory(int pageNumber, int pageSize, String sortBy, String sortDir, Long categoryId);
    ProductPageResponse searchByKeyword(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword);
    ProductResponse updateProduct(ProductDTO productDTO, Long productId);
    ProductResponse deleteProduct(Long productId);
}
