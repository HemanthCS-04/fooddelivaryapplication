package com.hungerbyte.service;

import com.hungerbyte.dto.AuthRequest;
import com.hungerbyte.dto.AuthResponse;
import com.hungerbyte.dto.RegisterRequest;
import com.hungerbyte.entity.Address;
import com.hungerbyte.entity.Cart;
import com.hungerbyte.entity.Role;
import com.hungerbyte.entity.User;
import com.hungerbyte.exception.DuplicateResourceException;
import com.hungerbyte.exception.InvalidRequestException;
import com.hungerbyte.exception.UnauthorizedException;
import com.hungerbyte.repository.AddressRepository;
import com.hungerbyte.repository.CartRepository;
import com.hungerbyte.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, AddressRepository addressRepository,
                       CartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with email " + request.getEmail() + " already exists");
        }

        if (request.getConfirmPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRequestException("Passwords do not match");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobile(request.getMobile());
        user.setRole(request.getRole() != null ? request.getRole() : Role.CUSTOMER);

        User savedUser = userRepository.save(user);

        // Create default empty cart for user
        Cart cart = new Cart(savedUser);
        cartRepository.save(cart);

        // If address provided during registration, save it
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            Address address = new Address();
            address.setStreet(request.getAddress().trim());
            address.setCity("Bangalore");
            address.setState("Karnataka");
            address.setPincode("560001");
            address.setAddressType("HOME");
            address.setIsDefault(true);
            address.setUser(savedUser);
            addressRepository.save(address);
        }

        String token = "HB-" + UUID.randomUUID().toString();
        return new AuthResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
                savedUser.getMobile(), savedUser.getRole(), token);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = "HB-" + UUID.randomUUID().toString();
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(),
                user.getMobile(), user.getRole(), token);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
