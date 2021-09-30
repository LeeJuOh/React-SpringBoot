package com.example.gccoffeeclone.order.repository;

import com.example.gccoffeeclone.order.model.Order;
import com.example.gccoffeeclone.order.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order insert(Order order);

    Optional<Order> findAcceptedOrderByEmail(String email, OrderStatus orderStatus);


    List<Order> findByStatusAndPeriod(OrderStatus orderStatus, LocalDateTime from,
        LocalDateTime to);

    void updateOrderStatus(List<byte[]> orderIds, OrderStatus orderStatus);

    void updateOrderAndOrderItems(Order order);

    void insertOrderItems(Order order);

    void deleteAllOrderItems(UUID orderId);
}
