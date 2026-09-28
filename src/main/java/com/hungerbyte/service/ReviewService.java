package com.hungerbyte.service;

import com.hungerbyte.dto.ReviewRequest;
import com.hungerbyte.entity.FoodItem;
import com.hungerbyte.entity.Restaurant;
import com.hungerbyte.entity.Review;
import com.hungerbyte.entity.User;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.FoodItemRepository;
import com.hungerbyte.repository.RestaurantRepository;
import com.hungerbyte.repository.ReviewRepository;
import com.hungerbyte.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository userRepository,
                         RestaurantRepository restaurantRepository,
                         FoodItemRepository foodItemRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public List<Review> getReviewsByFood(Long foodId) {
        return reviewRepository.findByFoodItemIdOrderByCreatedAtDesc(foodId);
    }

    public List<Review> getReviewsByRestaurant(Long restaurantId) {
        return reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Review addReview(Long userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Restaurant restaurant = null;
        if (request.getRestaurantId() != null) {
            restaurant = restaurantRepository.findById(request.getRestaurantId()).orElse(null);
        }

        FoodItem foodItem = null;
        if (request.getFoodItemId() != null) {
            foodItem = foodItemRepository.findById(request.getFoodItemId()).orElse(null);
        }

        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setFoodItem(foodItem);
        review.setRating(request.getRating());
        review.setComment(request.getComment().trim());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }
}
