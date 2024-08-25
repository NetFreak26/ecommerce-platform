package com.platform.ecommerce.products.services;

import com.platform.ecommerce.categories.models.Category;
import com.platform.ecommerce.categories.repositiories.CategoryRepository;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.products.models.Product;
import com.platform.ecommerce.products.models.ProductConfiguration;
import com.platform.ecommerce.products.payloads.ProductConfigurationDTO;
import com.platform.ecommerce.products.payloads.ProductDTO;
import com.platform.ecommerce.products.payloads.ProductPageResponse;
import com.platform.ecommerce.products.payloads.ProductResponse;
import com.platform.ecommerce.products.repositories.ProductConfigurationRepository;
import com.platform.ecommerce.products.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductConfigurationRepository productConfigurationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponse addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category does not exist with id: " + categoryId)
        );
        List<ProductConfiguration> productConfigurations = productDTO.getProductConfigurations()
                .stream()
                .map(this::findOrCreateProductConfiguration)
                .toList();

        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setProductConfigurations(productConfigurations);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    @Override
    public ProductPageResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productsPage = productRepository.findAll(pageable);

        return convertProductPageToResponse(productsPage);
    }

    @Override
    public ProductPageResponse getAllProductsByCategory(int pageNumber, int pageSize, String sortBy, String sortDir, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category does not exist with id: " + categoryId)
        );
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productsPage = productRepository.findAllByCategory(category, pageable);

        return convertProductPageToResponse(productsPage);
    }

    @Override
    public ProductPageResponse searchByKeyword(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productsPage = productRepository.findByKeyword(keyword, pageable);

        return convertProductPageToResponse(productsPage);
    }

    @Override
    public ProductResponse updateProduct(ProductDTO productDTO, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with product id: " + productId)
        );
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setSellingPrice(productDTO.getSellingPrice());

        List<ProductConfiguration> productConfigurations = productDTO.getProductConfigurations()
                .stream()
                .map(this::findOrCreateProductConfiguration)
                .toList();
        product.setProductConfigurations(productConfigurations);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    @Override
    public ProductResponse deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with product id: " + productId)
        );
        productRepository.delete(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    private ProductPageResponse convertProductPageToResponse(Page<Product> productsPage) {
        List<ProductResponse> productResponseList = productsPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class)).toList();

        ProductPageResponse pageResponse = new ProductPageResponse();
        pageResponse.setContent(productResponseList);
        pageResponse.setPageNumber(productsPage.getNumber());
        pageResponse.setPageSize(productsPage.getSize());
        pageResponse.setTotalPages(productsPage.getTotalPages());
        pageResponse.setTotalElements(productsPage.getTotalElements());
        pageResponse.setLastPage(productsPage.isLast());

        return pageResponse;
    }

    private ProductConfiguration findOrCreateProductConfiguration(ProductConfigurationDTO productConfigurationDTO) {

        Optional<ProductConfiguration> productConfiguration =
                productConfigurationRepository.findByNameAndValue(productConfigurationDTO.getName(), productConfigurationDTO.getValue());
        return productConfiguration.orElseGet(
                () -> productConfigurationRepository.save(modelMapper.map(productConfigurationDTO, ProductConfiguration.class))
        );
    }
}
