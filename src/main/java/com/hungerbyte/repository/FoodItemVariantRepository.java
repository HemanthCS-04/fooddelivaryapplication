package com.hungerbyte.repository;

import com.hungerbyte.entity.FoodItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemVariantRepository extends JpaRepository<FoodItemVariant, Long> {
    List<FoodItemVariant> findByFoodItemId(Long foodItemId);
}
