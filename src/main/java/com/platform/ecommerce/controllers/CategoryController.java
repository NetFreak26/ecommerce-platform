package com.platform.ecommerce.controllers;

import com.platform.ecommerce.dtos.CategoryDto;
import com.platform.ecommerce.exceptions.DuplicationException;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createProductCategory(@Valid @RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto categoryDto1 = categoryService.createProductCategory(categoryDto);
            return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
        } catch (DuplicationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProductCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getAllProductCategories();
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable Long categoryId) {
        try {
            CategoryDto deletedCategory = categoryService.deleteProductCategory(categoryId);
            return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
