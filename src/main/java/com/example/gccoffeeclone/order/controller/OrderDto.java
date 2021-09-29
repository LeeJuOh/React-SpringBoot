package com.example.gccoffeeclone.order.controller;

import com.example.gccoffeeclone.order.model.Email;
import com.example.gccoffeeclone.order.model.Order;
import com.example.gccoffeeclone.order.model.OrderItem;
import com.example.gccoffeeclone.order.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderDto {

    private UUID orderId;
    @NotNull
    private String email;
    @NotBlank
    private String address;
    @NotBlank
    private String postcode;
    @Size(min = 1)
    private List<@Valid OrderItemDto> orderItems;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order toEntity() {
        return new Order(
            UUID.randomUUID(),
            new Email(email),
            address,
            postcode,
            orderItems.stream()
                .map(orderItemDto ->
                    new OrderItem(
                        orderItemDto.getProductId(),
                        orderItemDto.getCategory(),
                        orderItemDto.getPrice(),
                        orderItemDto.getQuantity()))
                .collect(Collectors.toList()),
            OrderStatus.ACCEPTED,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderItemDto> getOrderItems() {
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

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setOrderItems(
        List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
