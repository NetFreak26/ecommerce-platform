package com.platform.ecommerce.security.services;

import com.platform.ecommerce.users.models.User;
import com.platform.ecommerce.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(identifier, identifier).orElseThrow(
                () -> new UsernameNotFoundException("No user found with username or email: " + identifier)
        );
        return UserDetailsImpl.build(user);
    }
}
