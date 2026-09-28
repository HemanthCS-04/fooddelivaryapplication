package com.hungerbyte.repository;

import com.hungerbyte.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFoodItemIdOrderByCreatedAtDesc(Long foodItemId);
    List<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
}
