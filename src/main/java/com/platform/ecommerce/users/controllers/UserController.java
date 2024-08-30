package com.platform.ecommerce.users.controllers;

import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.users.models.Address;
import com.platform.ecommerce.users.payloads.AddressDTO;
import com.platform.ecommerce.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add/address")
    public ResponseEntity<?> addUserAddress(@Valid @RequestBody AddressDTO addressDTO) {
        try {
            AddressDTO savedAddressDTO = userService.addUserAddress(addressDTO);
            return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
        } catch (UsernameNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getUserAddresses() {
        try {
            List<AddressDTO> addressDTOS = userService.getUserAddresses();
            return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<?> getUserAddress(@PathVariable Long addressId) {
        try {
            AddressDTO addressDTO = userService.getUserAddress(addressId);
            return new ResponseEntity<>(addressDTO, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/address/{addressId}")
    public ResponseEntity<?> updateUserAddress(@Valid @RequestBody AddressDTO addressDTO, @PathVariable Long addressId) {
        try {
            AddressDTO updateUserAddress = userService.updateUserAddress(addressDTO, addressId);
            return new ResponseEntity<>(updateUserAddress, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/address/{addressId}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable Long addressId) {
        try {
            userService.deleteUserAddress(addressId);
            return new ResponseEntity<>("Address Deleted Successfully", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
    }
}
