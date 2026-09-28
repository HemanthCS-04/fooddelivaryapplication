package com.hungerbyte.service;

import com.hungerbyte.entity.Favorite;
import com.hungerbyte.entity.FoodItem;
import com.hungerbyte.entity.Restaurant;
import com.hungerbyte.entity.User;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.FavoriteRepository;
import com.hungerbyte.repository.FoodItemRepository;
import com.hungerbyte.repository.RestaurantRepository;
import com.hungerbyte.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           RestaurantRepository restaurantRepository,
                           FoodItemRepository foodItemRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public List<Favorite> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Favorite addFavorite(Long userId, Long restaurantId, Long foodItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Restaurant restaurant = null;
        if (restaurantId != null) {
            restaurant = restaurantRepository.findById(restaurantId).orElse(null);
            var existing = favoriteRepository.findByUserIdAndRestaurantId(userId, restaurantId);
            if (existing.isPresent()) return existing.get();
        }

        FoodItem foodItem = null;
        if (foodItemId != null) {
            foodItem = foodItemRepository.findById(foodItemId).orElse(null);
            var existing = favoriteRepository.findByUserIdAndFoodItemId(userId, foodItemId);
            if (existing.isPresent()) return existing.get();
        }

        Favorite fav = new Favorite(user, restaurant, foodItem);
        return favoriteRepository.save(fav);
    }

    @Transactional
    public void removeFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
