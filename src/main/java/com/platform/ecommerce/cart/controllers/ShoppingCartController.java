package com.platform.ecommerce.cart.controllers;

import com.platform.ecommerce.cart.payloads.ShoppingCartItemDTO;
import com.platform.ecommerce.cart.payloads.ShoppingCartResponse;
import com.platform.ecommerce.cart.services.ShoppingCartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseEntity<?> addSoppingCartItem(@Valid @RequestBody ShoppingCartItemDTO shoppingCartItemDTO) {
        try {
            shoppingCartService.addSoppingCartItem(shoppingCartItemDTO);
            return new ResponseEntity<>("Item added successfully", HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/cart")
    public ResponseEntity<?> getUserShoppingCart() {
        try {
            ShoppingCartResponse shoppingCartResponse = shoppingCartService.getUserCart();
            return new ResponseEntity<>(shoppingCartResponse, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update/item/{itemId}/quantity/{quantity}")
    public ResponseEntity<?> updateProduct(@PathVariable Long itemId, @PathVariable Integer quantity) {
        try {
            ShoppingCartResponse shoppingCartResponse = shoppingCartService.updateShoppingCartItem(itemId, quantity);
            return new ResponseEntity<>(shoppingCartResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/item/{itemId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long itemId) {
        try {
            ShoppingCartResponse shoppingCartResponse = shoppingCartService.deleteShoppingCartItem(itemId);
            return new ResponseEntity<>(shoppingCartResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
    }

}
