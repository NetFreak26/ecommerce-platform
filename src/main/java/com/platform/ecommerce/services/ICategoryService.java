package com.platform.ecommerce.services;

import com.platform.ecommerce.payloads.CategoryDTO;
import com.platform.ecommerce.payloads.CategoryPageResponse;

public interface ICategoryService {
    CategoryDTO createProductCategory(CategoryDTO categoryDto);
    CategoryPageResponse getAllProductCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
    CategoryDTO deleteProductCategory(Long categoryId);
}
