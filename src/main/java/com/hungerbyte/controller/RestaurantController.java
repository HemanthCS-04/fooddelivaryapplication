package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.dto.RestaurantRequest;
import com.hungerbyte.entity.Restaurant;
import com.hungerbyte.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Restaurant>>> getAllRestaurants(
            @RequestParam(required = false) String search) {
        List<Restaurant> list;
        if (search != null && !search.trim().isEmpty()) {
            list = restaurantService.search(search);
        } else {
            list = restaurantService.getAllRestaurants();
        }
        return ResponseEntity.ok(ApiResponse.ok("Restaurants retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurant>> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(ApiResponse.ok("Restaurant details retrieved", restaurant));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Restaurant>> createRestaurant(
            @Valid @RequestBody RestaurantRequest request,
            @RequestParam(required = false) Long ownerId) {
        Restaurant restaurant = restaurantService.createRestaurant(request, ownerId);
        return ResponseEntity.ok(ApiResponse.ok("Restaurant added successfully", restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurant>> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request) {
        Restaurant updated = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Restaurant updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok(ApiResponse.ok("Restaurant deleted successfully"));
    }
}
