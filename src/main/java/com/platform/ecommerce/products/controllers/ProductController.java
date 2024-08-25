package com.platform.ecommerce.products.controllers;

import com.platform.ecommerce.config.AppConstants;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.products.payloads.ProductDTO;
import com.platform.ecommerce.products.payloads.ProductPageResponse;
import com.platform.ecommerce.products.payloads.ProductResponse;
import com.platform.ecommerce.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add/category/{categoryId}")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {
        try {
            ProductResponse addedProduct = productService.addProduct(productDTO, categoryId);
            return new ResponseEntity<>(addedProduct, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir
    ) {
        ProductPageResponse pageResponse = productService.getAllProducts(
                pageNumber, pageSize, sortBy, sortDir
        );
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/all/category/{categoryId}")
    public ResponseEntity<?> getAllProductsByCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir,

            @PathVariable Long categoryId
    ) {
        try {
            ProductPageResponse pageResponse = productService.getAllProductsByCategory(
                    pageNumber, pageSize, sortBy, sortDir, categoryId
            );
            return new ResponseEntity<>(pageResponse, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all/search/{keyword}")
    public ResponseEntity<?> getAllProductsByKeyword(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir,

            @PathVariable String keyword
    ) {
        ProductPageResponse pageResponse = productService.searchByKeyword(
                pageNumber, pageSize, sortBy, sortDir, keyword
        );
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long productId) {
        try {
            ProductResponse updatedProduct = productService.updateProduct(productDTO, productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            ProductResponse deletedProduct = productService.deleteProduct(productId);
            return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
