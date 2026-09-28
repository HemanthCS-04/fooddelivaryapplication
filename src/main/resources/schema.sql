-- ===================================================================
-- HungerByte Food Delivery Application - MySQL Schema & Seed Script
-- Database: hungerbyte_db
-- Compatible with MySQL 8.0+ / MySQL Workbench
-- ===================================================================

CREATE DATABASE IF NOT EXISTS `hungerbyte_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hungerbyte_db`;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(120) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `mobile` VARCHAR(20),
    `role` ENUM('CUSTOMER', 'ADMIN', 'RESTAURANT_OWNER') NOT NULL DEFAULT 'CUSTOMER',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_users_email` (`email`),
    INDEX `idx_users_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Addresses Table
CREATE TABLE IF NOT EXISTS `addresses` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `address_line` VARCHAR(255) NOT NULL,
    `city` VARCHAR(100) NOT NULL,
    `state` VARCHAR(100) NOT NULL,
    `pincode` VARCHAR(20) NOT NULL,
    `landmark` VARCHAR(150),
    `address_type` VARCHAR(50) DEFAULT 'Home',
    `is_default` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_addresses_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Restaurants Table
CREATE TABLE IF NOT EXISTS `restaurants` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `owner_id` BIGINT,
    `name` VARCHAR(150) NOT NULL,
    `description` TEXT,
    `cuisine` VARCHAR(150),
    `address` VARCHAR(255),
    `phone` VARCHAR(30),
    `image_url` VARCHAR(500),
    `rating` DECIMAL(2,1) DEFAULT 4.5,
    `total_reviews` INT DEFAULT 0,
    `delivery_time_mins` INT DEFAULT 30,
    `cost_for_two` DECIMAL(10,2) DEFAULT 400.00,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    INDEX `idx_restaurants_rating` (`rating`),
    INDEX `idx_restaurants_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Categories Table
CREATE TABLE IF NOT EXISTS `categories` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `description` VARCHAR(255),
    `image_url` VARCHAR(500),
    `is_active` BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Food Items Table
CREATE TABLE IF NOT EXISTS `food_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `restaurant_id` BIGINT NOT NULL,
    `category_id` BIGINT,
    `name` VARCHAR(150) NOT NULL,
    `description` TEXT,
    `price` DECIMAL(10,2) NOT NULL,
    `image_url` VARCHAR(500),
    `is_veg` BOOLEAN DEFAULT TRUE,
    `is_available` BOOLEAN DEFAULT TRUE,
    `rating` DECIMAL(2,1) DEFAULT 4.5,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`) ON DELETE SET NULL,
    INDEX `idx_food_restaurant` (`restaurant_id`),
    INDEX `idx_food_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Food Item Variants Table
CREATE TABLE IF NOT EXISTS `food_item_variants` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `food_item_id` BIGINT NOT NULL,
    `variant_name` VARCHAR(100) NOT NULL,
    `price_delta` DECIMAL(10,2) DEFAULT 0.00,
    `is_available` BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (`food_item_id`) REFERENCES `food_items`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Carts Table
CREATE TABLE IF NOT EXISTS `carts` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Cart Items Table
CREATE TABLE IF NOT EXISTS `cart_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `cart_id` BIGINT NOT NULL,
    `food_item_id` BIGINT NOT NULL,
    `variant_id` BIGINT,
    `quantity` INT NOT NULL DEFAULT 1,
    `price_per_item` DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (`cart_id`) REFERENCES `carts`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`food_item_id`) REFERENCES `food_items`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`variant_id`) REFERENCES `food_item_variants`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Coupons Table
CREATE TABLE IF NOT EXISTS `coupons` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50) NOT NULL UNIQUE,
    `discount_percentage` DECIMAL(5,2) DEFAULT 0,
    `flat_discount_amount` DECIMAL(10,2) DEFAULT 0,
    `min_order_amount` DECIMAL(10,2) DEFAULT 0,
    `max_discount_amount` DECIMAL(10,2) DEFAULT 100,
    `is_active` BOOLEAN DEFAULT TRUE,
    `expiry_date` DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Orders Table
CREATE TABLE IF NOT EXISTS `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_number` VARCHAR(50) NOT NULL UNIQUE,
    `user_id` BIGINT NOT NULL,
    `restaurant_id` BIGINT NOT NULL,
    `address_id` BIGINT,
    `order_status` ENUM('PLACED', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED') DEFAULT 'PLACED',
    `subtotal` DECIMAL(10,2) NOT NULL,
    `delivery_fee` DECIMAL(10,2) DEFAULT 40.00,
    `tax_amount` DECIMAL(10,2) DEFAULT 0.00,
    `discount_amount` DECIMAL(10,2) DEFAULT 0.00,
    `total_amount` DECIMAL(10,2) NOT NULL,
    `coupon_code` VARCHAR(50),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants`(`id`),
    FOREIGN KEY (`address_id`) REFERENCES `addresses`(`id`) ON DELETE SET NULL,
    INDEX `idx_orders_user` (`user_id`),
    INDEX `idx_orders_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Order Items Table
CREATE TABLE IF NOT EXISTS `order_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `food_item_id` BIGINT NOT NULL,
    `food_name` VARCHAR(150) NOT NULL,
    `variant_name` VARCHAR(100),
    `price` DECIMAL(10,2) NOT NULL,
    `quantity` INT NOT NULL,
    `subtotal` DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. Payments Table
CREATE TABLE IF NOT EXISTS `payments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL UNIQUE,
    `transaction_id` VARCHAR(100) NOT NULL UNIQUE,
    `payment_method` ENUM('UPI', 'CARD', 'NETBANKING', 'COD') NOT NULL,
    `payment_status` ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'SUCCESS',
    `amount` DECIMAL(10,2) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 13. Reviews Table
CREATE TABLE IF NOT EXISTS `reviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `restaurant_id` BIGINT NOT NULL,
    `rating` INT NOT NULL CHECK (`rating` BETWEEN 1 AND 5),
    `comment` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 14. Favorites Table
CREATE TABLE IF NOT EXISTS `favorites` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `restaurant_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_restaurant` (`user_id`, `restaurant_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
