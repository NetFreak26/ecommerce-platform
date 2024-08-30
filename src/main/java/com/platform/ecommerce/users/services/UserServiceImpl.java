package com.platform.ecommerce.users.services;

import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.users.models.Address;
import com.platform.ecommerce.users.models.User;
import com.platform.ecommerce.users.payloads.AddressDTO;
import com.platform.ecommerce.users.repositories.AddressRepository;
import com.platform.ecommerce.users.repositories.UserRepository;
import com.platform.ecommerce.util.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO addUserAddress(AddressDTO addressDTO) {
        User user = authUtils.loggedInUser();
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        user.addAddress(savedAddress);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public AddressDTO getUserAddress(Long addressId) {
        User user = authUtils.loggedInUser();
        Address address = addressRepository.findByAddressIdAndUser(addressId, user).orElseThrow(
                () -> new ResourceNotFoundException("User address doesn't exist with address Id: " + addressId)
        );
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses() {
        User user = authUtils.loggedInUser();
        return user.getAddresses()
                .stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateUserAddress(AddressDTO addressDTO, Long addressId) {
        User user = authUtils.loggedInUser();
        Address address = addressRepository.findByAddressIdAndUser(addressId, user).orElseThrow(
                () -> new ResourceNotFoundException("User address doesn't exist with address Id: " + addressId)
        );
        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setPincode(addressDTO.getPincode());
        address.setCity(address.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());

        Address updatedAddress = addressRepository.save(address);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public void deleteUserAddress(Long addressId) {
        User user = authUtils.loggedInUser();
        Address address = addressRepository.findByAddressIdAndUser(addressId, user).orElseThrow(
                () -> new ResourceNotFoundException("User address doesn't exist with address Id: " + addressId)
        );
        user.removeAddress(address);
        addressRepository.delete(address);
    }
}
