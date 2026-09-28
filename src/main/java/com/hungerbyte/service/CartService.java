package com.hungerbyte.service;

import com.hungerbyte.dto.CartItemRequest;
import com.hungerbyte.entity.*;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodItemRepository foodItemRepository;
    private final FoodItemVariantRepository variantRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       FoodItemRepository foodItemRepository,
                       FoodItemVariantRepository variantRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.foodItemRepository = foodItemRepository;
        this.variantRepository = variantRepository;
        this.userRepository = userRepository;
    }

    public Cart getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(Long userId, CartItemRequest request) {
        Cart cart = getCartByUser(userId);
        FoodItem foodItem = foodItemRepository.findById(request.getFoodItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + request.getFoodItemId()));

        FoodItemVariant variant = null;
        double itemPrice = foodItem.getPrice();

        if (request.getVariantId() != null) {
            variant = variantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + request.getVariantId()));
            itemPrice = variant.getPrice();
        }

        // Check if item already in cart
        Optional<CartItem> existingItem;
        if (variant != null) {
            existingItem = cartItemRepository.findByCartIdAndFoodItemIdAndVariantId(cart.getId(), foodItem.getId(), variant.getId());
        } else {
            existingItem = cartItemRepository.findByCartIdAndFoodItemIdAndVariantIsNull(cart.getId(), foodItem.getId());
        }

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, foodItem, variant, request.getQuantity(), itemPrice);
            cartItemRepository.save(newItem);
        }

        return getCartByUser(userId);
    }

    @Transactional
    public Cart updateCartItemQuantity(Long userId, Long cartItemId, int quantity) {
        Cart cart = getCartByUser(userId);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("Cart item does not belong to user's cart");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return getCartByUser(userId);
    }

    @Transactional
    public Cart removeCartItem(Long userId, Long cartItemId) {
        Cart cart = getCartByUser(userId);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("Cart item does not belong to user's cart");
        }

        cartItemRepository.delete(item);
        return getCartByUser(userId);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUser(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }
}
