package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.dto.FoodItemRequest;
import com.hungerbyte.entity.FoodItem;
import com.hungerbyte.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@CrossOrigin(origins = "*")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodItem>>> getAllFoods(
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search) {

        List<FoodItem> list;
        if (restaurantId != null) {
            list = foodItemService.getFoodsByRestaurant(restaurantId);
        } else if (categoryId != null) {
            list = foodItemService.getFoodsByCategory(categoryId);
        } else if (search != null && !search.trim().isEmpty()) {
            list = foodItemService.searchFoods(search);
        } else {
            list = foodItemService.getAllFoods();
        }
        return ResponseEntity.ok(ApiResponse.ok("Food items retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodItem>> getFoodById(@PathVariable Long id) {
        FoodItem item = foodItemService.getFoodById(id);
        return ResponseEntity.ok(ApiResponse.ok("Food details retrieved", item));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FoodItem>> createFood(@Valid @RequestBody FoodItemRequest request) {
        FoodItem created = foodItemService.createFood(request);
        return ResponseEntity.ok(ApiResponse.ok("Food item added successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodItem>> updateFood(
            @PathVariable Long id,
            @Valid @RequestBody FoodItemRequest request) {
        FoodItem updated = foodItemService.updateFood(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Food item updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(@PathVariable Long id) {
        foodItemService.deleteFood(id);
        return ResponseEntity.ok(ApiResponse.ok("Food item deleted successfully"));
    }
}
