package com.platform.ecommerce.categories.services;

import com.platform.ecommerce.categories.payloads.CategoryDTO;
import com.platform.ecommerce.categories.payloads.CategoryPageResponse;

public interface CategoryService {
    CategoryDTO createProductCategory(CategoryDTO categoryDto);
    CategoryPageResponse getAllProductCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
    CategoryDTO deleteProductCategory(Long categoryId);
}
