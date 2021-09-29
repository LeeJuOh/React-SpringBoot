package com.example.gccoffeeclone.order.repository;

import com.example.gccoffeeclone.order.model.Email;
import com.example.gccoffeeclone.order.model.Order;
import com.example.gccoffeeclone.order.model.OrderItem;
import com.example.gccoffeeclone.order.model.OrderStatus;
import com.example.gccoffeeclone.utils.JDBCUtils;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class OrderJdbcRepository implements OrderRepository {

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = JDBCUtils.toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        var updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        return new Order(orderId, email, address, postcode, orderStatus, createdAt, updatedAt);

    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order insert(Order order) {
        jdbcTemplate.update(
            "INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) "
                +
                "VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
            toOrderParamMap(order));
        order.getOrderItems()
            .forEach(item ->
                jdbcTemplate.update(
                    "INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) "
                        +
                        "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                    toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(),
                        order.getUpdatedAt(), item)));
        return order;
    }

    @Override
    public List<Order> findByStatusAndPeriod(OrderStatus orderStatus, LocalDateTime from,
        LocalDateTime to) {
        return jdbcTemplate.query(
            "SELECT * FROM orders WHERE order_status = :orderStatus AND updated_at >= :from AND updated_at <= :to",
            Map.of(
                "orderStatus", orderStatus.name(),
                "from", Timestamp.valueOf(from),
                "to", Timestamp.valueOf(to)),
            orderRowMapper
        );
    }

    @Override
    public void updateOrderStatus(List<byte[]> orderIds, OrderStatus orderStatus) {

        SqlParameterSource parameters = new MapSqlParameterSource("orderIds", orderIds)
            .addValue("orderStatus", orderStatus.name());
        var update = jdbcTemplate.update(
            "UPDATE orders SET order_status = :orderStatus WHERE BIN_TO_UUID(order_id) IN (:orderIds)",
            parameters);
        if(update > 0) {
            log.info("Paid order's status changed to Ready for Delivery");
        }
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt,
        LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.productId().toString().getBytes());
        paramMap.put("category", item.category().toString());
        paramMap.put("price", item.price());
        paramMap.put("quantity", item.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }
}


