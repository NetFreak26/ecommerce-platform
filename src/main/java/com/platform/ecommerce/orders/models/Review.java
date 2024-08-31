package com.platform.ecommerce.orders.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platform.ecommerce.users.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "order_item_id")
    @JsonIgnore
    private OrderItem orderItem;

    private Double rating;

    @Size(min = 5, max = 250)
    private String comment;
}
