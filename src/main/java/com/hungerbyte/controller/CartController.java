package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.dto.CartItemRequest;
import com.hungerbyte.entity.Cart;
import com.hungerbyte.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCart(@RequestParam(defaultValue = "3") Long userId) {
        Cart cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(ApiResponse.ok("Cart retrieved", cart));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Cart>> addItemToCart(
            @RequestParam(defaultValue = "3") Long userId,
            @Valid @RequestBody CartItemRequest request) {
        Cart cart = cartService.addItemToCart(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Item added to cart", cart));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Cart>> updateItemQuantity(
            @PathVariable Long id,
            @RequestParam int quantity,
            @RequestParam(defaultValue = "3") Long userId) {
        Cart cart = cartService.updateCartItemQuantity(userId, id, quantity);
        return ResponseEntity.ok(ApiResponse.ok("Cart updated", cart));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Cart>> removeItem(
            @PathVariable Long id,
            @RequestParam(defaultValue = "3") Long userId) {
        Cart cart = cartService.removeCartItem(userId, id);
        return ResponseEntity.ok(ApiResponse.ok("Item removed from cart", cart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@RequestParam(defaultValue = "3") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.ok("Cart cleared"));
    }
}
