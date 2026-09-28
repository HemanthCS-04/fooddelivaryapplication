package com.hungerbyte.service;

import com.hungerbyte.dto.OrderRequest;
import com.hungerbyte.entity.*;
import com.hungerbyte.exception.InvalidRequestException;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        PaymentRepository paymentRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        RestaurantRepository restaurantRepository,
                        UserRepository userRepository,
                        CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Order> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with number: " + orderNumber));
    }

    @Transactional
    public Order placeOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidRequestException("Cart is empty"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new InvalidRequestException("Cart is empty. Please add items before placing an order.");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        if (request.getDeliveryAddress() == null || request.getDeliveryAddress().trim().isEmpty()) {
            throw new InvalidRequestException("Delivery address is required");
        }

        // Calculate subtotal
        double subtotal = 0.0;
        for (CartItem item : cart.getItems()) {
            subtotal += item.getSubtotal();
        }

        double deliveryFee = 40.0;
        double tax = Math.round((subtotal * 0.05) * 100.0) / 100.0; // 5% GST
        double discount = 0.0;

        // Apply coupon if valid
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            String code = request.getCouponCode().trim().toUpperCase();
            var couponOpt = couponRepository.findByCodeIgnoreCaseAndIsActiveTrue(code);
            if (couponOpt.isPresent()) {
                discount = couponOpt.get().calculateDiscount(subtotal);
            }
        }

        double totalAmount = Math.max(0.0, Math.round((subtotal + deliveryFee + tax - discount) * 100.0) / 100.0);

        // Create Order
        Order order = new Order();
        String orderNumber = "HB" + System.currentTimeMillis();
        order.setOrderNumber(orderNumber);
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(request.getDeliveryAddress().trim());
        order.setOrderStatus(Order.OrderStatus.PLACED);
        order.setSubtotal(subtotal);
        order.setDeliveryFee(deliveryFee);
        order.setTax(tax);
        order.setDiscount(discount);
        order.setTotalAmount(totalAmount);
        order.setCouponCode(request.getCouponCode());
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Create Order Items
        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder);
            oi.setFoodItem(ci.getFoodItem());
            oi.setFoodName(ci.getFoodItem().getName());
            oi.setVariantName(ci.getVariant() != null ? ci.getVariant().getName() : null);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getPrice());
            orderItemRepository.save(oi);
        }

        // Create Payment
        String txnId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = new Payment(savedOrder, totalAmount, request.getPaymentMethod(),
                Payment.PaymentStatus.COMPLETED, txnId);
        paymentRepository.save(payment);
        savedOrder.setPayment(payment);

        // Clear user's cart
        cartItemRepository.deleteByCartId(cart.getId());

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
}
