package com.hungerbyte.service;

import com.hungerbyte.dto.DashboardStatsDto;
import com.hungerbyte.entity.Order;
import com.hungerbyte.entity.Role;
import com.hungerbyte.entity.User;
import com.hungerbyte.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;
    private final OrderRepository orderRepository;

    public AdminService(UserRepository userRepository,
                        RestaurantRepository restaurantRepository,
                        FoodItemRepository foodItemRepository,
                        OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
        this.orderRepository = orderRepository;
    }

    public DashboardStatsDto getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalCustomers = userRepository.findByRole(Role.CUSTOMER).size();
        long totalRestaurants = restaurantRepository.count();
        long totalFoodItems = foodItemRepository.count();
        List<Order> allOrders = orderRepository.findAll();
        long totalOrders = allOrders.size();

        double totalRevenue = allOrders.stream()
                .filter(o -> o.getOrderStatus() != Order.OrderStatus.CANCELLED)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        long activeOrders = allOrders.stream()
                .filter(o -> o.getOrderStatus() != Order.OrderStatus.DELIVERED && o.getOrderStatus() != Order.OrderStatus.CANCELLED)
                .count();

        return new DashboardStatsDto(totalUsers, totalCustomers, totalRestaurants,
                totalFoodItems, totalOrders, Math.round(totalRevenue * 100.0) / 100.0, activeOrders);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
