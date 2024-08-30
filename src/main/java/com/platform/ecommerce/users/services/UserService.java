package com.platform.ecommerce.users.services;

import com.platform.ecommerce.users.payloads.AddressDTO;

import java.util.List;

public interface UserService {
    AddressDTO addUserAddress(AddressDTO addressDTO);

    AddressDTO getUserAddress(Long addressId);

    List<AddressDTO> getUserAddresses();

    AddressDTO updateUserAddress(AddressDTO addressDTO, Long addressId);

    void deleteUserAddress(Long addressId);
}
