package com.platform.ecommerce.categories.services;

import com.platform.ecommerce.categories.payloads.CategoryDTO;
import com.platform.ecommerce.categories.payloads.CategoryPageResponse;
import com.platform.ecommerce.exceptions.DuplicationException;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.categories.models.Category;
import com.platform.ecommerce.categories.repositiories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createProductCategory(CategoryDTO categoryDto) {
        Category category = categoryRepository.findByCategoryName(categoryDto.getCategoryName());

        if (category != null) {
            throw new DuplicationException("Category Already Exist: " + categoryDto.getCategoryName());
        }
        category = modelMapper.map(categoryDto, Category.class);
        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    @Override
    public CategoryPageResponse getAllProductCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
//        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, direction, sortBy);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);

        List<CategoryDTO> categoryDTOList = page.getContent().stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryPageResponse categoryPageResponse = new CategoryPageResponse();
        categoryPageResponse.setContent(categoryDTOList);
        categoryPageResponse.setPageNumber(page.getNumber());
        categoryPageResponse.setPageSize(page.getSize());
        categoryPageResponse.setTotalPages(page.getTotalPages());
        categoryPageResponse.setTotalElements(page.getTotalElements());
        categoryPageResponse.setLastPage(page.isLast());
        return categoryPageResponse;
    }

    @Override
    public CategoryDTO deleteProductCategory(Long categoryId) {
        Category categoryToDelete = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found with id: " + categoryId)
        );
        categoryRepository.delete(categoryToDelete);
        return modelMapper.map(categoryToDelete, CategoryDTO.class);
    }
}
