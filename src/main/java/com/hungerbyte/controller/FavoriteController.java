package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.entity.Favorite;
import com.hungerbyte.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Favorite>>> getFavorites(@RequestParam(defaultValue = "3") Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Favorites retrieved", favoriteService.getFavoritesByUser(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Favorite>> addFavorite(
            @RequestParam(defaultValue = "3") Long userId,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) Long foodItemId) {
        Favorite fav = favoriteService.addFavorite(userId, restaurantId, foodItemId);
        return ResponseEntity.ok(ApiResponse.ok("Added to favorites", fav));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(@PathVariable Long id) {
        favoriteService.removeFavorite(id);
        return ResponseEntity.ok(ApiResponse.ok("Removed from favorites"));
    }
}
