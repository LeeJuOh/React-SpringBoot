package com.example.gccoffeeclone.order.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Order {

    private final UUID orderId;
    private final Email email;
    private String address;
    private String postcode;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(UUID orderId, Email email, String address, String postcode,
        List<OrderItem> orderItems, OrderStatus orderStatus, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(UUID orderId, Email email, String address, String postcode,
        OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order mergeOrder(Order otherOrder) {
        address = otherOrder.getAddress();
        postcode = otherOrder.getPostcode();

        // create new order items
        var otherOrderItems = otherOrder.getOrderItems().stream()
            .collect(Collectors.toMap(OrderItem::productId, orderItem -> orderItem));

        List<OrderItem> newOrderItems = new ArrayList<>(
            orderItems.size() + otherOrder.getOrderItems().size());
        orderItems.forEach(orderItem -> {
            if (otherOrderItems.containsKey(orderItem.productId())) {
                var duplicatedOrderItem = otherOrderItems.get(orderItem.productId());
                newOrderItems.add(new OrderItem(
                    orderItem.productId(),
                    orderItem.category(),
                    orderItem.price(),
                    orderItem.quantity() + duplicatedOrderItem.quantity()));
                otherOrderItems.remove(orderItem.productId());
            } else {
                newOrderItems.add(orderItem);
            }
        });
        newOrderItems.addAll(otherOrderItems.values());
        orderItems = newOrderItems;

        setUpdatedAt();
        return this;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Email getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setAddress(String address) {
        this.address = address;
        setUpdatedAt();
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
        setUpdatedAt();
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        setUpdatedAt();
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", email=" + email +
            ", address='" + address + '\'' +
            ", postcode='" + postcode + '\'' +
            ", orderItems=" + orderItems +
            ", orderStatus=" + orderStatus +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

