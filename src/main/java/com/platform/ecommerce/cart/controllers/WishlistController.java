package com.platform.ecommerce.cart.controllers;

import com.platform.ecommerce.cart.payloads.WishlistResponse;
import com.platform.ecommerce.cart.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;
    @PostMapping("/add/product/{productId}")
    public ResponseEntity<?> addWishlistItem(@PathVariable Long productId) {
        try {
            wishlistService.addWishlistItem(productId);
            return new ResponseEntity<>("Item added successfully", HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserWishlist() {
        try {
            WishlistResponse wishlistResponse = wishlistService.getUserWishlist();
            return new ResponseEntity<>(wishlistResponse, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/item/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        try {
            WishlistResponse wishlistResponse = wishlistService.deleteItem(itemId);
            return new ResponseEntity<>(wishlistResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
