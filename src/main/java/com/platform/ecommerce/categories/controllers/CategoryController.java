package com.platform.ecommerce.categories.controllers;

import com.platform.ecommerce.config.AppConstants;
import com.platform.ecommerce.categories.payloads.CategoryDTO;
import com.platform.ecommerce.categories.payloads.CategoryPageResponse;
import com.platform.ecommerce.exceptions.DuplicationException;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.categories.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/create")
    public ResponseEntity<?> createProductCategory(@Valid @RequestBody CategoryDTO categoryDto) {
        try {
            CategoryDTO categoryDTO1 = categoryService.createProductCategory(categoryDto);
            return new ResponseEntity<>(categoryDTO1, HttpStatus.CREATED);
        } catch (DuplicationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProductCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir
    ) {
        CategoryPageResponse categoryPageResponse = categoryService.getAllProductCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categoryPageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable Long categoryId) {
        try {
            CategoryDTO deletedCategory = categoryService.deleteProductCategory(categoryId);
            return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
