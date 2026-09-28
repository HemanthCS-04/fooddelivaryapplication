package com.hungerbyte.dto;

import com.hungerbyte.entity.Order;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull(message = "Order status is required")
    private Order.OrderStatus status;

    public OrderStatusUpdateRequest() {}

    public OrderStatusUpdateRequest(Order.OrderStatus status) {
        this.status = status;
    }

    public Order.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Order.OrderStatus status) {
        this.status = status;
    }
}
