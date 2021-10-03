package com.example.gccoffeeclone.order.scheduler;


import com.example.gccoffeeclone.order.model.OrderStatus;
import com.example.gccoffeeclone.order.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class OrderScheduler {

    private final OrderRepository orderRepository;

    public OrderScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul")
    public void changeOrderStatusToReadyForDelivery() {
        log.info("order delivery cron job start");
        var to = LocalDateTime.now()
            .withMinute(0)
            .withSecond(0)
            .withNano(0);
        var from = to.minusHours(24);

        var orders = orderRepository.findByStatusAndPeriod(
            OrderStatus.PAYMENT_CONFIRMED,
            from,
            to);

        if (!orders.isEmpty()) {
            log.info("[from {} ~ to {}] paid orders: {}", from, to, orders);
            var orderIds = orders.stream()
                .map(order -> order.getOrderId().toString().getBytes())
                .collect(Collectors.toList());
            orderRepository.updateOrderStatus(orderIds, OrderStatus.READY_FOR_DELIVERY);
        }
    }

}
