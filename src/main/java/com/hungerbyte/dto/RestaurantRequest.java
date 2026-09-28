package com.hungerbyte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RestaurantRequest {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String description;

    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @NotBlank(message = "Address is required")
    private String address;

    private String phone;

    private String imageUrl;

    @NotNull(message = "Delivery time is required")
    private Integer deliveryTimeMins;

    @NotNull(message = "Cost for two is required")
    private Double costForTwo;

    public RestaurantRequest() {}

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
}
