package com.example.gccoffeeclone.order.repository;

import com.example.gccoffeeclone.order.model.Order;
import com.example.gccoffeeclone.order.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository {

    Order insert(Order order);

    List<Order> findByStatusAndPeriod(OrderStatus orderStatus, LocalDateTime from,
        LocalDateTime to);

    void updateOrderStatus(List<byte[]> orderIds, OrderStatus orderStatus);

}
