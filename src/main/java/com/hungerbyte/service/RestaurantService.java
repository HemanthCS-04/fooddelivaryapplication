package com.hungerbyte.service;

import com.hungerbyte.dto.RestaurantRequest;
import com.hungerbyte.entity.Restaurant;
import com.hungerbyte.entity.User;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.RestaurantRepository;
import com.hungerbyte.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
    }

    public List<Restaurant> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllRestaurants();
        }
        return restaurantRepository.searchRestaurants(query.trim());
    }

    public List<Restaurant> getByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public Restaurant createRestaurant(RestaurantRequest request, Long ownerId) {
        User owner = null;
        if (ownerId != null) {
            owner = userRepository.findById(ownerId).orElse(null);
        }

        Restaurant r = new Restaurant();
        r.setName(request.getName().trim());
        r.setDescription(request.getDescription());
        r.setCuisine(request.getCuisine().trim());
        r.setAddress(request.getAddress().trim());
        r.setPhone(request.getPhone());
        r.setImageUrl(request.getImageUrl() != null && !request.getImageUrl().isEmpty()
                ? request.getImageUrl()
                : "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=600&auto=format&fit=crop&q=80");
        r.setDeliveryTimeMins(request.getDeliveryTimeMins());
        r.setCostForTwo(request.getCostForTwo());
        r.setRating(4.5);
        r.setTotalReviews(1);
        r.setOwner(owner);
        r.setIsActive(true);

        return restaurantRepository.save(r);
    }

    @Transactional
    public Restaurant updateRestaurant(Long id, RestaurantRequest request) {
        Restaurant r = getRestaurantById(id);
        r.setName(request.getName().trim());
        r.setDescription(request.getDescription());
        r.setCuisine(request.getCuisine().trim());
        r.setAddress(request.getAddress().trim());
        r.setPhone(request.getPhone());
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            r.setImageUrl(request.getImageUrl());
        }
        r.setDeliveryTimeMins(request.getDeliveryTimeMins());
        r.setCostForTwo(request.getCostForTwo());
        return restaurantRepository.save(r);
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant r = getRestaurantById(id);
        r.setIsActive(false);
        restaurantRepository.save(r);
    }
}
