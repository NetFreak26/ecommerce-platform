package com.platform.ecommerce.products.services;

import com.platform.ecommerce.products.payloads.ImageData;
import com.platform.ecommerce.products.payloads.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    ProductResponse saveProductImage(Long productId, MultipartFile file);
    ImageData downloadImage(Long imageId);
}
