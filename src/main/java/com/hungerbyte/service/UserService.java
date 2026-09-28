package com.hungerbyte.service;

import com.hungerbyte.entity.Address;
import com.hungerbyte.entity.User;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.AddressRepository;
import com.hungerbyte.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User updateUser(Long id, User updateData) {
        User user = getUserById(id);
        if (updateData.getName() != null) user.setName(updateData.getName());
        if (updateData.getMobile() != null) user.setMobile(updateData.getMobile());
        return userRepository.save(user);
    }

    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Transactional
    public Address addAddress(Long userId, Address address) {
        User user = getUserById(userId);
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
