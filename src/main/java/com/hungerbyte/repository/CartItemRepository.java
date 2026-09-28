package com.hungerbyte.repository;

import com.hungerbyte.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    Optional<CartItem> findByCartIdAndFoodItemIdAndVariantId(Long cartId, Long foodItemId, Long variantId);
    Optional<CartItem> findByCartIdAndFoodItemIdAndVariantIsNull(Long cartId, Long foodItemId);
    void deleteByCartId(Long cartId);
}
