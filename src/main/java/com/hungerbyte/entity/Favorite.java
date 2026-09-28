package com.hungerbyte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "restaurant_id"}),
    @UniqueConstraint(columnNames = {"user_id", "food_item_id"})
})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Favorite() {}

    public Favorite(User user, Restaurant restaurant, FoodItem foodItem) {
        this.user = user;
        this.restaurant = restaurant;
        this.foodItem = foodItem;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
