package com.platform.ecommerce.orders.payloads;

import com.platform.ecommerce.orders.models.PaymentMethod;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderRequest {
    @NonNull
    private PaymentMethod paymentMethod;

    @NonNull
    private String paymentInfo;
}
