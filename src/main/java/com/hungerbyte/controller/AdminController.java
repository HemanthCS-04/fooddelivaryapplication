package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.dto.DashboardStatsDto;
import com.hungerbyte.entity.Order;
import com.hungerbyte.entity.Restaurant;
import com.hungerbyte.entity.User;
import com.hungerbyte.service.AdminService;
import com.hungerbyte.service.OrderService;
import com.hungerbyte.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;
    private final RestaurantService restaurantService;

    public AdminController(AdminService adminService,
                           OrderService orderService,
                           RestaurantService restaurantService) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.ok("Dashboard stats retrieved", adminService.getDashboardStats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok("All users retrieved", adminService.getAllUsers()));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.ok("All orders retrieved", orderService.getAllOrders()));
    }

    @GetMapping("/restaurants")
    public ResponseEntity<ApiResponse<List<Restaurant>>> getAllRestaurants() {
        return ResponseEntity.ok(ApiResponse.ok("All restaurants retrieved", restaurantService.getAllRestaurants()));
    }
}
