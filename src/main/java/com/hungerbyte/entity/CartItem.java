package com.hungerbyte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id")
    private FoodItemVariant variant;

    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;

    @NotNull
    private Double price;

    public CartItem() {}

    public CartItem(Cart cart, FoodItem foodItem, FoodItemVariant variant, Integer quantity, Double price) {
        this.cart = cart;
        this.foodItem = foodItem;
        this.variant = variant;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public FoodItemVariant getVariant() {
        return variant;
    }

    public void setVariant(FoodItemVariant variant) {
        this.variant = variant;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubtotal() {
        return (price != null ? price : 0.0) * (quantity != null ? quantity : 1);
    }
}
