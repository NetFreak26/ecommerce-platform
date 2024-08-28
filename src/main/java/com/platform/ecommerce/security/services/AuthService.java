package com.platform.ecommerce.security.services;

import com.platform.ecommerce.exceptions.DuplicationException;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.security.jwt.JwtUtils;
import com.platform.ecommerce.security.payloads.LoginRequest;
import com.platform.ecommerce.security.payloads.SignupRequest;
import com.platform.ecommerce.security.payloads.UserInfoResponse;
import com.platform.ecommerce.users.models.AppRole;
import com.platform.ecommerce.users.models.Role;
import com.platform.ecommerce.users.models.User;
import com.platform.ecommerce.users.repositories.RoleRepository;
import com.platform.ecommerce.users.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserInfoResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserInfoResponse userInfoResponse = modelMapper.map(userDetails, UserInfoResponse.class);
        userInfoResponse.setJwtToken(jwtUtils.generateJwtTokenFromUsername(userDetails.getUsername()));
        return userInfoResponse;
    }

    public UserInfoResponse signUp(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new DuplicationException("Username already exist: " + signupRequest.getUsername());
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new DuplicationException("Email already exist: " + signupRequest.getEmail());
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setIsEnabled(true);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        List<String> strRoles = signupRequest.getRoles();

        if (strRoles != null) {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByAppRole(AppRole.ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        user.getRoles().add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepository.findByAppRole(AppRole.SELLER)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        user.getRoles().add(modRole);

                        break;
                    case "user":
                        Role userRole = roleRepository.findByAppRole(AppRole.USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        user.getRoles().add(userRole);

                        break;
                }
            });
        }
        User savedUser = userRepository.save(user);

        UserInfoResponse userInfoResponse = modelMapper.map(savedUser, UserInfoResponse.class);
        userInfoResponse.setJwtToken(jwtUtils.generateJwtTokenFromUsername(user.getUsername()));
        return userInfoResponse;
    }
}
