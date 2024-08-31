package com.platform.ecommerce.orders.services.strategy;

public interface PaymentStrategy {
    boolean processPayment(String token, double amount);
}
