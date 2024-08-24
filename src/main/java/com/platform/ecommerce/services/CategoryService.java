package com.platform.ecommerce.services;

import com.platform.ecommerce.dtos.CategoryDto;
import com.platform.ecommerce.exceptions.DuplicationException;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.models.Category;
import com.platform.ecommerce.repositiories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createProductCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findByCategoryName(categoryDto.getCategoryName());

        if (category != null) {
            throw new DuplicationException("Category Already Exist: " + categoryDto.getCategoryName());
        }
        category = modelMapper.map(categoryDto, Category.class);
        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllProductCategories() {
        return categoryRepository.findAll().stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
    }

    @Override
    public CategoryDto deleteProductCategory(Long categoryId) {
        Category categoryToDelete = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found with id: " + categoryId)
        );
        categoryRepository.delete(categoryToDelete);
        return modelMapper.map(categoryToDelete, CategoryDto.class);
    }
}
