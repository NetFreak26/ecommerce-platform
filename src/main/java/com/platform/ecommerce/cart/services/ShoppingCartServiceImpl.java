package com.platform.ecommerce.cart.services;

import com.platform.ecommerce.cart.models.ShoppingCart;
import com.platform.ecommerce.cart.models.ShoppingCartItem;
import com.platform.ecommerce.cart.payloads.ShoppingCartItemDTO;
import com.platform.ecommerce.cart.payloads.ShoppingCartItemResponse;
import com.platform.ecommerce.cart.payloads.ShoppingCartResponse;
import com.platform.ecommerce.cart.repositories.ShoppingCartItemRepository;
import com.platform.ecommerce.cart.repositories.ShoppingCartRepository;
import com.platform.ecommerce.exceptions.DuplicationException;
import com.platform.ecommerce.exceptions.ProductNotAvailableException;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.products.models.Product;
import com.platform.ecommerce.products.repositories.ProductRepository;
import com.platform.ecommerce.users.models.User;
import com.platform.ecommerce.util.AuthUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public void addShoppingCartItem(ShoppingCartItemDTO shoppingCartItemDTO) {
        User user = authUtils.loggedInUser();
        Product product = productRepository.findById(shoppingCartItemDTO.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + shoppingCartItemDTO.getProductId())
        );
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        Optional<ShoppingCartItem> shoppingCartItemOptional = shoppingCartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);

        if (shoppingCartItemOptional.isPresent()) {
            throw new DuplicationException("Product Already exist in the cart: " + product.getProductName());
        }

        if (product.getQuantity() == 0) {
            throw new ProductNotAvailableException("Product Not Available " + product.getProductName());
        }

        if (product.getQuantity() < shoppingCartItemDTO.getQuantity()) {
            throw new ProductNotAvailableException("Available quantity of product is " + product.getQuantity() + ". Please add less quantity.");
        }

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setShoppingCart(shoppingCart);
        shoppingCartItem.setProduct(product);
        shoppingCartItem.setQuantity(shoppingCartItemDTO.getQuantity());

        shoppingCartItemRepository.save(shoppingCartItem);

        shoppingCart.addShoppingCartItem(shoppingCartItem);

    }

    @Override
    public ShoppingCartResponse getUserCart() {
        User user = authUtils.loggedInUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        return mapShoppingCartToResponse(shoppingCart);
    }

    @Override
    public ShoppingCartResponse updateShoppingCartItem(Long itemId, Integer quantity) {
        User user = authUtils.loggedInUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);

        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findByItemIdAndShoppingCart(itemId, shoppingCart).orElseThrow(
                () -> new ResourceNotFoundException("Shopping Cart Item Not Found with in user's cart: " + itemId)
        );

        synchronized (shoppingCartItem) {
            if (quantity <= 0) {
                throw new RuntimeException("Quantity can not be set less than or equal to 0, delete the product if you want to set it to 0.");
            }

            Product product = shoppingCartItem.getProduct();

            if (product.getQuantity() == 0) {
                throw new ProductNotAvailableException("Product Not Available " + product.getProductName() +". Please delete the product from you cart.");
            }

            if (product.getQuantity() < quantity) {
                throw new ProductNotAvailableException("Available quantity of product is " + product.getQuantity() + ". Please update the quantity to less number.");
            }

            shoppingCartItem.setQuantity(quantity);
            shoppingCartItemRepository.save(shoppingCartItem);
            return mapShoppingCartToResponse(shoppingCart);
        }
    }

    @Override
    public ShoppingCartResponse deleteShoppingCartItem(Long itemId) {
        User user = authUtils.loggedInUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);

        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findByItemIdAndShoppingCart(itemId, shoppingCart).orElseThrow(
                () -> new ResourceNotFoundException("Shopping Cart Item Not Found with in user's cart: " + itemId)
        );

        shoppingCart.removeShoppingCartItem(shoppingCartItem);
        shoppingCartItemRepository.delete(shoppingCartItem);
        return mapShoppingCartToResponse(shoppingCart);
    }

    private ShoppingCartResponse mapShoppingCartToResponse(ShoppingCart shoppingCart) {
        List<ShoppingCartItemResponse> shoppingCartItemResponses = shoppingCart.getShoppingCartItems()
                .stream()
                .map(item -> modelMapper.map(item, ShoppingCartItemResponse.class))
                .toList();
        return new ShoppingCartResponse(shoppingCartItemResponses);
    }
}
