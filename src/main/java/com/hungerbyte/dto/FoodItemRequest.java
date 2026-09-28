package com.hungerbyte.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FoodItemRequest {

    @NotBlank(message = "Food name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin("0.0")
    private Double price;

    private String imageUrl;

    private Boolean isVeg = true;

    private Boolean isAvailable = true;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    public FoodItemRequest() {}

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

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
