package com.platform.ecommerce.cart.services;

import com.platform.ecommerce.cart.models.ShoppingCartItem;
import com.platform.ecommerce.cart.models.Wishlist;
import com.platform.ecommerce.cart.models.WishlistItem;
import com.platform.ecommerce.cart.payloads.WishlistItemResponse;
import com.platform.ecommerce.cart.payloads.WishlistResponse;
import com.platform.ecommerce.cart.repositories.WishlistItemRepository;
import com.platform.ecommerce.cart.repositories.WishlistRepository;
import com.platform.ecommerce.exceptions.DuplicationException;
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
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addWishlistItem(Long productId) {
        User user = authUtils.loggedInUser();
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + productId)
        );
        Wishlist wishlist = wishlistRepository.findByUser(user);
        Optional<WishlistItem> wishlistItemOptional = wishlistItemRepository.findByWishlistAndProduct(wishlist, product);

        if (wishlistItemOptional.isPresent()) {
            throw new DuplicationException("Product Already exist in the wishlist: " + product.getProductName());
        }

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setWishlist(wishlist);
        wishlistItem.setProduct(product);

        wishlistItemRepository.save(wishlistItem);

        wishlist.addWishlistItem(wishlistItem);
    }

    @Override
    public WishlistResponse getUserWishlist() {
        User user = authUtils.loggedInUser();
        Wishlist wishlist = wishlistRepository.findByUser(user);
        return mapWishlistToWishlistResponse(wishlist);
    }

    @Override
    public WishlistResponse deleteItem(Long itemId) {
        User user = authUtils.loggedInUser();
        Wishlist wishlist = wishlistRepository.findByUser(user);
        WishlistItem wishlistItem = wishlistItemRepository.findByItemIdAndWishlist(itemId, wishlist).orElseThrow(
                () -> new ResourceNotFoundException("Wishlist Item Not Found with in user's wishlist: " + itemId)
        );
        wishlist.removeWishlistItem(wishlistItem);
        wishlistItemRepository.delete(wishlistItem);
        return mapWishlistToWishlistResponse(wishlist);
    }

    private WishlistResponse mapWishlistToWishlistResponse(Wishlist wishlist) {
        List<WishlistItemResponse> wishlistItemResponses = wishlist.getWishlistItems()
                .stream()
                .map(wishlistItem -> modelMapper.map(wishlistItem, WishlistItemResponse.class))
                .toList();
        return new WishlistResponse(wishlistItemResponses);
    }
}
