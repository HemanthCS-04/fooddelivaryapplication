package com.hungerbyte.repository;

import com.hungerbyte.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByIsActiveTrue();
    List<Restaurant> findByOwnerId(Long ownerId);

    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.cuisine) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.address) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Restaurant> searchRestaurants(@Param("keyword") String keyword);
}
