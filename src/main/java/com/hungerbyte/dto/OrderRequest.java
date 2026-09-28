package com.hungerbyte.dto;

import com.hungerbyte.entity.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotNull(message = "Payment method is required")
    private Payment.PaymentMethod paymentMethod;

    private String couponCode;

    public OrderRequest() {}

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Payment.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
