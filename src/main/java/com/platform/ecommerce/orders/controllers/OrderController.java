package com.platform.ecommerce.orders.controllers;

import com.platform.ecommerce.orders.payloads.OrderRequest;
import com.platform.ecommerce.orders.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place/address/{addressId}")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequest orderRequest, @PathVariable Long addressId) {
        try {
            orderService.placeOrder(orderRequest, addressId);
            return ResponseEntity.ok().body("Order placed successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
