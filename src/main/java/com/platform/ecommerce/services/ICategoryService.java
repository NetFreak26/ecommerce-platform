package com.platform.ecommerce.services;

import com.platform.ecommerce.dtos.CategoryDto;

import java.util.List;

public interface ICategoryService {
    CategoryDto createProductCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllProductCategories();
    CategoryDto deleteProductCategory(Long categoryId);
}
