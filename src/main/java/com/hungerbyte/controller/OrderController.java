package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.dto.OrderRequest;
import com.hungerbyte.dto.OrderStatusUpdateRequest;
import com.hungerbyte.entity.Order;
import com.hungerbyte.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> placeOrder(
            @RequestParam(defaultValue = "3") Long userId,
            @Valid @RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Order placed successfully!", order));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long restaurantId) {
        List<Order> orders;
        if (userId != null) {
            orders = orderService.getOrdersByUser(userId);
        } else if (restaurantId != null) {
            orders = orderService.getOrdersByRestaurant(restaurantId);
        } else {
            orders = orderService.getAllOrders();
        }
        return ResponseEntity.ok(ApiResponse.ok("Orders retrieved", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.ok("Order details retrieved", order));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<Order>> getOrderByNumber(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.ok("Order details retrieved", order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        Order order = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.ok("Order status updated to " + request.getStatus(), order));
    }
}
