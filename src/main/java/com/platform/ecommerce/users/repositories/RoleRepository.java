package com.platform.ecommerce.users.repositories;

import com.platform.ecommerce.users.models.AppRole;
import com.platform.ecommerce.users.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAppRole(AppRole appRole);
}
