package com.platform.ecommerce.orders.models;

import com.platform.ecommerce.users.models.Address;
import com.platform.ecommerce.users.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private LocalDate orderDate;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "order",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public synchronized void addOrderItem(OrderItem orderItem) {
        if (this.orderItems.contains(orderItem)) {
            return;
        }
        this.orderItems.add(orderItem);
    }

    public synchronized void removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId);
    }
}
