package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.entity.Address;
import com.hungerbyte.entity.User;
import com.hungerbyte.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("User retrieved", userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(ApiResponse.ok("User updated", userService.updateUser(id, user)));
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<List<Address>>> getAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Addresses retrieved", userService.getUserAddresses(id)));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<ApiResponse<Address>> addAddress(@PathVariable Long id, @Valid @RequestBody Address address) {
        return ResponseEntity.ok(ApiResponse.ok("Address added", userService.addAddress(id, address)));
    }

    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id, @PathVariable Long addressId) {
        userService.deleteAddress(addressId);
        return ResponseEntity.ok(ApiResponse.ok("Address deleted"));
    }
}
