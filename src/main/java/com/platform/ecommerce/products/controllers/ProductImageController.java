package com.platform.ecommerce.products.controllers;

import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.products.payloads.ImageData;
import com.platform.ecommerce.products.payloads.ProductResponse;
import com.platform.ecommerce.products.services.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping("/{productId}/image")
    public ResponseEntity<?> uploadProductImage(@PathVariable Long productId,
                                                                  @RequestParam("file") MultipartFile file) {
        try {
            ProductResponse productResponse = productImageService.saveProductImage(productId, file);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable Long imageId) {
        try {
            ImageData imageData = productImageService.downloadImage(imageId);
            Resource imageResource = imageData.getResource();
            String fileName = imageData.getFileName();
            MediaType mediaType = imageData.getMediaType();

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(imageResource);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
