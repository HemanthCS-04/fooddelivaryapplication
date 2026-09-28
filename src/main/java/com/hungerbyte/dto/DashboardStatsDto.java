package com.hungerbyte.dto;

public class DashboardStatsDto {
    private long totalUsers;
    private long totalCustomers;
    private long totalRestaurants;
    private long totalFoodItems;
    private long totalOrders;
    private double totalRevenue;
    private long activeOrders;

    public DashboardStatsDto() {}

    public DashboardStatsDto(long totalUsers, long totalCustomers, long totalRestaurants,
                             long totalFoodItems, long totalOrders, double totalRevenue, long activeOrders) {
        this.totalUsers = totalUsers;
        this.totalCustomers = totalCustomers;
        this.totalRestaurants = totalRestaurants;
        this.totalFoodItems = totalFoodItems;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.activeOrders = activeOrders;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalRestaurants() {
        return totalRestaurants;
    }

    public void setTotalRestaurants(long totalRestaurants) {
        this.totalRestaurants = totalRestaurants;
    }

    public long getTotalFoodItems() {
        return totalFoodItems;
    }

    public void setFoodItems(long totalFoodItems) {
        this.totalFoodItems = totalFoodItems;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(long activeOrders) {
        this.activeOrders = activeOrders;
    }
}
