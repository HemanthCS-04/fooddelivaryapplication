package com.hungerbyte.repository;

import com.hungerbyte.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByRestaurantId(Long restaurantId);
    List<FoodItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);
    List<FoodItem> findByCategoryId(Long categoryId);
    List<FoodItem> findByIsVeg(Boolean isVeg);

    @Query("SELECT f FROM FoodItem f WHERE f.isAvailable = true AND " +
           "(LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<FoodItem> searchFoods(@Param("keyword") String keyword);
}
