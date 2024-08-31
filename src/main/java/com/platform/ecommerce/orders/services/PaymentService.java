package com.platform.ecommerce.orders.services;

import com.platform.ecommerce.orders.models.PaymentMethod;
import com.platform.ecommerce.orders.services.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentStrategyFactory paymentStrategyFactory;

    public PaymentService(PaymentStrategyFactory paymentStrategyFactory) {
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    public boolean processPayment(PaymentMethod method, String info, double amount) {
        PaymentStrategy strategy = paymentStrategyFactory.getPaymentStrategy(method);
        return strategy.processPayment(info, amount);
    }
}
