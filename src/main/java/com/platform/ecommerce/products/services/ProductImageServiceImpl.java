package com.platform.ecommerce.products.services;

import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.products.models.Product;
import com.platform.ecommerce.products.models.ProductImage;
import com.platform.ecommerce.products.payloads.ImageData;
import com.platform.ecommerce.products.payloads.ProductResponse;
import com.platform.ecommerce.products.repositories.ProductImageRepository;
import com.platform.ecommerce.products.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponse saveProductImage(Long productId, MultipartFile file) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + productId)
        );

        try {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setFileName(file.getOriginalFilename());
            productImage.setData(file.getBytes());
            ProductImage savedProductImage = productImageRepository.save(productImage);

            product.addProductImage(savedProductImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }

        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public ImageData downloadImage(Long imageId) {
        ProductImage productImage = productImageRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("Image not exist with id: " + imageId)
        );

        ByteArrayResource resource = new ByteArrayResource(productImage.getData());
        MediaType mediaType = determineMediaType(productImage.getFileName());
        return new ImageData(resource, mediaType,  productImage.getFileName());
    }

    private MediaType determineMediaType(String fileName) {
        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        return MediaType.APPLICATION_OCTET_STREAM; // Default type
    }
}
