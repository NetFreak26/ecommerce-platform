package com.platform.ecommerce.users.repositories;

import com.platform.ecommerce.users.models.Address;
import com.platform.ecommerce.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddressIdAndUser(Long addressId, User user);
}
