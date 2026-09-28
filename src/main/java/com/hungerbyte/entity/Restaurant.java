package com.hungerbyte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Restaurant name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotBlank(message = "Cuisine type is required")
    @Column(nullable = false, length = 150)
    private String cuisine; // e.g. "Biryani, North Indian", "Italian, Fast Food"

    @Column(nullable = false)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    private Double rating = 4.5;

    @Column(name = "total_reviews")
    private Integer totalReviews = 10;

    @Column(name = "delivery_time_mins")
    private Integer deliveryTimeMins = 30;

    @Column(name = "cost_for_two")
    private Double costForTwo = 400.0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FoodItem> foodItems = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Restaurant() {}

    public Restaurant(String name, String description, String cuisine, String address, String phone,
                      String imageUrl, Double rating, Integer totalReviews, Integer deliveryTimeMins,
                      Double costForTwo, User owner) {
        this.name = name;
        this.description = description;
        this.cuisine = cuisine;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.totalReviews = totalReviews;
        this.deliveryTimeMins = deliveryTimeMins;
        this.costForTwo = costForTwo;
        this.owner = owner;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Integer getDeliveryTimeMins() {
        return deliveryTimeMins;
    }

    public void setDeliveryTimeMins(Integer deliveryTimeMins) {
        this.deliveryTimeMins = deliveryTimeMins;
    }

    public Double getCostForTwo() {
        return costForTwo;
    }

    public void setCostForTwo(Double costForTwo) {
        this.costForTwo = costForTwo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
