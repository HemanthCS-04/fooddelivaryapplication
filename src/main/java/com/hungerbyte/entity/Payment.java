package com.hungerbyte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }

    public enum PaymentMethod {
        CASH_ON_DELIVERY,
        UPI,
        CREDIT_CARD,
        DEBIT_CARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    @JsonIgnore
    private Order order;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus = PaymentStatus.COMPLETED;

    @Column(name = "transaction_id", nullable = false, length = 100)
    private String transactionId;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate = LocalDateTime.now();

    public Payment() {}

    public Payment(Order order, Double amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, String transactionId) {
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.paymentDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
