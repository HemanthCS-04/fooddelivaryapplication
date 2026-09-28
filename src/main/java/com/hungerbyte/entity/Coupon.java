package com.hungerbyte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon code is required")
    @Column(nullable = false, unique = true, length = 50)
    private String code; // e.g. "HUNGER50", "FEAST100"

    @Column(length = 255)
    private String description;

    @NotNull
    @Column(name = "discount_type", nullable = false, length = 20)
    private String discountType = "FLAT"; // "FLAT" or "PERCENTAGE"

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Column(name = "min_order_amount")
    private Double minOrderAmount = 0.0;

    @Column(name = "max_discount_amount")
    private Double maxDiscountAmount;

    @Column(name = "valid_until")
    private LocalDate validUntil;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public Coupon() {}

    public Coupon(String code, String description, String discountType, Double discountValue,
                  Double minOrderAmount, Double maxDiscountAmount, LocalDate validUntil) {
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.validUntil = validUntil;
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Double getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(Double maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double calculateDiscount(Double orderAmount) {
        if (!Boolean.TRUE.equals(isActive)) return 0.0;
        if (minOrderAmount != null && orderAmount < minOrderAmount) return 0.0;

        double discount = 0.0;
        if ("PERCENTAGE".equalsIgnoreCase(discountType)) {
            discount = (orderAmount * discountValue) / 100.0;
            if (maxDiscountAmount != null && discount > maxDiscountAmount) {
                discount = maxDiscountAmount;
            }
        } else {
            discount = discountValue;
        }
        return Math.min(discount, orderAmount);
    }
}
