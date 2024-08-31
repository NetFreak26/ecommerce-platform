package com.platform.ecommerce.orders.services;

import com.platform.ecommerce.orders.models.PaymentMethod;
import com.platform.ecommerce.orders.services.strategy.CardPaymentStrategy;
import com.platform.ecommerce.orders.services.strategy.PaymentStrategy;
import com.platform.ecommerce.orders.services.strategy.UPIPaymentStrategy;
import com.platform.ecommerce.orders.services.strategy.WalletPaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {
    private final CardPaymentStrategy cardPaymentStrategy;
    private final UPIPaymentStrategy upiPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    @Autowired
    public PaymentStrategyFactory(CardPaymentStrategy cardPaymentStrategy,
                                  UPIPaymentStrategy upiPaymentStrategy,
                                  WalletPaymentStrategy walletPaymentStrategy) {
        this.cardPaymentStrategy = cardPaymentStrategy;
        this.upiPaymentStrategy = upiPaymentStrategy;
        this.walletPaymentStrategy = walletPaymentStrategy;
    }
    public PaymentStrategy getPaymentStrategy(PaymentMethod method) {

        return switch (method) {
            case CARD -> cardPaymentStrategy;
            case UPI -> upiPaymentStrategy;
            case DIGITAL_WALLET -> walletPaymentStrategy;
        };
    }
}
