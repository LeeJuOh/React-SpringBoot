package com.example.gccoffeeclone.order.service;


import com.example.gccoffeeclone.order.controller.OrderDto;
import com.example.gccoffeeclone.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@Slf4j
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void createOrder(OrderDto orderDto) {
        var order = orderDto.toEntity();
        orderRepository.insert(order);
    }

}
