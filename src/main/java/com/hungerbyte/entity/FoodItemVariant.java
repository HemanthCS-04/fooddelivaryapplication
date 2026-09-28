package com.hungerbyte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "food_item_variants")
public class FoodItemVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Variant name is required (e.g. Regular, Medium, Large)")
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull(message = "Variant price is required")
    @Column(nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id", nullable = false)
    @JsonIgnore
    private FoodItem foodItem;

    public FoodItemVariant() {}

    public FoodItemVariant(String name, Double price, FoodItem foodItem) {
        this.name = name;
        this.price = price;
        this.foodItem = foodItem;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }
}
