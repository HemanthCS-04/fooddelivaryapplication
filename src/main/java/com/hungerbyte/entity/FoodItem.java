package com.hungerbyte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_items")
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false)
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_veg")
    private Boolean isVeg = true;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    private Double rating = 4.5;

    @Column(name = "total_reviews")
    private Integer totalReviews = 10;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodItemVariant> variants = new ArrayList<>();

    public FoodItem() {}

    public FoodItem(String name, String description, Double price, String imageUrl,
                    Boolean isVeg, Boolean isAvailable, Double rating,
                    Restaurant restaurant, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isVeg = isVeg;
        this.isAvailable = isAvailable;
        this.rating = rating;
        this.restaurant = restaurant;
        this.category = category;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Boolean isVeg) {
        this.isVeg = isVeg;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<FoodItemVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<FoodItemVariant> variants) {
        this.variants = variants;
    }
}
