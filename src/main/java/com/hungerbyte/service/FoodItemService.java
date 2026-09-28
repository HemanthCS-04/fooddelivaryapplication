package com.hungerbyte.service;

import com.hungerbyte.dto.FoodItemRequest;
import com.hungerbyte.entity.Category;
import com.hungerbyte.entity.FoodItem;
import com.hungerbyte.entity.Restaurant;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.CategoryRepository;
import com.hungerbyte.repository.FoodItemRepository;
import com.hungerbyte.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    public FoodItemService(FoodItemRepository foodItemRepository,
                           RestaurantRepository restaurantRepository,
                           CategoryRepository categoryRepository) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<FoodItem> getAllFoods() {
        return foodItemRepository.findAll();
    }

    public FoodItem getFoodById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
    }

    public List<FoodItem> getFoodsByRestaurant(Long restaurantId) {
        return foodItemRepository.findByRestaurantId(restaurantId);
    }

    public List<FoodItem> getFoodsByCategory(Long categoryId) {
        return foodItemRepository.findByCategoryId(categoryId);
    }

    public List<FoodItem> searchFoods(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllFoods();
        }
        return foodItemRepository.searchFoods(keyword.trim());
    }

    @Transactional
    public FoodItem createFood(FoodItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + request.getRestaurantId()));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        FoodItem item = new FoodItem();
        item.setName(request.getName().trim());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl() != null && !request.getImageUrl().isEmpty()
                ? request.getImageUrl()
                : "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80");
        item.setIsVeg(request.getIsVeg() != null ? request.getIsVeg() : true);
        item.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
        item.setRestaurant(restaurant);
        item.setCategory(category);
        item.setRating(4.5);
        item.setTotalReviews(1);

        return foodItemRepository.save(item);
    }

    @Transactional
    public FoodItem updateFood(Long id, FoodItemRequest request) {
        FoodItem existing = getFoodById(id);
        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            existing.setImageUrl(request.getImageUrl());
        }
        if (request.getIsVeg() != null) {
            existing.setIsVeg(request.getIsVeg());
        }
        if (request.getIsAvailable() != null) {
            existing.setIsAvailable(request.getIsAvailable());
        }
        if (request.getCategoryId() != null) {
            Category cat = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            existing.setCategory(cat);
        }
        return foodItemRepository.save(existing);
    }

    @Transactional
    public void deleteFood(Long id) {
        FoodItem existing = getFoodById(id);
        existing.setIsAvailable(false);
        foodItemRepository.save(existing);
    }
}
