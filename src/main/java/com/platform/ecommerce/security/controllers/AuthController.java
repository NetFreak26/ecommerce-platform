package com.platform.ecommerce.security.controllers;

import com.platform.ecommerce.cart.models.ShoppingCart;
import com.platform.ecommerce.cart.models.Wishlist;
import com.platform.ecommerce.exceptions.DuplicationException;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.security.jwt.JwtUtils;
import com.platform.ecommerce.security.payloads.LoginRequest;
import com.platform.ecommerce.security.payloads.SignupRequest;
import com.platform.ecommerce.security.payloads.UserInfoResponse;
import com.platform.ecommerce.security.services.UserDetailsImpl;
import com.platform.ecommerce.users.models.AppRole;
import com.platform.ecommerce.users.models.Role;
import com.platform.ecommerce.users.models.User;
import com.platform.ecommerce.users.repositories.RoleRepository;
import com.platform.ecommerce.users.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
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

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            UserInfoResponse userInfoResponse = modelMapper.map(userDetails, UserInfoResponse.class);

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(userInfoResponse);
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        try {
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

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);

            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);

            user.setShoppingCart(shoppingCart);
            user.setWishlist(wishlist);
            userRepository.save(user);

            return new ResponseEntity<>("User Created Successfully", HttpStatus.CREATED);
        } catch (DuplicationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body("You've been signed out!");
    }
}
