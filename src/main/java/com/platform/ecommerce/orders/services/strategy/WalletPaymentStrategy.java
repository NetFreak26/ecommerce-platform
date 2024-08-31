package com.platform.ecommerce.orders.services.strategy;

import org.springframework.stereotype.Service;

@Service
public class WalletPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(String token, double amount) {
        return true;
    }
}
