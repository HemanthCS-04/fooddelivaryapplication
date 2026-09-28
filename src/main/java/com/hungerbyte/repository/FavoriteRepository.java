package com.hungerbyte.repository;

import com.hungerbyte.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Favorite> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
    Optional<Favorite> findByUserIdAndFoodItemId(Long userId, Long foodItemId);
    void deleteByUserIdAndRestaurantId(Long userId, Long restaurantId);
    void deleteByUserIdAndFoodItemId(Long userId, Long foodItemId);
}
