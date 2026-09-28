package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.dto.ReviewRequest;
import com.hungerbyte.entity.Review;
import com.hungerbyte.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> addReview(
            @RequestParam(defaultValue = "3") Long userId,
            @Valid @RequestBody ReviewRequest request) {
        Review review = reviewService.addReview(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Review submitted successfully", review));
    }

    @GetMapping("/food/{foodId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByFood(@PathVariable Long foodId) {
        return ResponseEntity.ok(ApiResponse.ok("Food reviews retrieved", reviewService.getReviewsByFood(foodId)));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(ApiResponse.ok("Restaurant reviews retrieved", reviewService.getReviewsByRestaurant(restaurantId)));
    }
}
