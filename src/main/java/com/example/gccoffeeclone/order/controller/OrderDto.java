package com.example.gccoffeeclone.order.controller;

import static com.example.gccoffeeclone.utils.MapperUtils.getModelMapper;

import com.example.gccoffeeclone.order.model.Email;
import com.example.gccoffeeclone.order.model.Order;
import com.example.gccoffeeclone.order.model.OrderItem;
import com.example.gccoffeeclone.order.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderDto {
    private UUID orderId;
    @NotNull
    private Email email;
    @NotBlank
    private String address;
    @NotBlank
    private String postcode;
    @Size(min = 1)
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderDto from(Order order) {
        return getModelMapper().map(order, OrderDto.class);
    }

    public Order toEntity() {
        return new Order(
            UUID.randomUUID(),
            email,
            address,
            postcode,
            orderItems,
            OrderStatus.ACCEPTED,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
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

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
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
