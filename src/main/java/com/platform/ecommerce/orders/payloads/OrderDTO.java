package com.platform.ecommerce.orders.payloads;

import com.platform.ecommerce.orders.models.OrderStatus;
import com.platform.ecommerce.users.payloads.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private int orderId;
    private AddressDTO address;
    private LocalDate orderDate;

    private List<OrderItemDTO> orderItems;
    private PaymentDTO payment;
    private Double totalAmount;
    private OrderStatus orderStatus;
}
