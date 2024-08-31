package com.platform.ecommerce.orders.services;

import com.platform.ecommerce.orders.payloads.OrderRequest;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest, Long addressId);
}
