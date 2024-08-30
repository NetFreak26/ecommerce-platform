package com.platform.ecommerce.users.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platform.ecommerce.cart.models.ShoppingCart;
import com.platform.ecommerce.cart.models.Wishlist;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String username;

    private Boolean isEnabled;

    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "user",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            orphanRemoval = true)
    @JsonIgnore
    private ShoppingCart shoppingCart;

    @OneToOne(mappedBy = "user",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            orphanRemoval = true)
    @JsonIgnore
    private Wishlist wishlist;

    public synchronized void addRole(Role role) {
        if (this.roles.contains(role)) {
            return;
        }
        this.roles.add(role);
    }

    public synchronized void removeRole(Role role) {
        this.roles.remove(role);
    }

    public synchronized void addAddress(Address address) {
        if (this.addresses.contains(address)) {
            return;
        }
        this.addresses.add(address);
    }

    public synchronized void removeAddress(Address address) {
        this.addresses.remove(address);
    }
}
