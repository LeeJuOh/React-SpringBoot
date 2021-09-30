package com.example.gccoffeeclone.order.repository;

import static com.example.gccoffeeclone.utils.JDBCUtils.toUUID;

import com.example.gccoffeeclone.order.model.Email;
import com.example.gccoffeeclone.order.model.Order;
import com.example.gccoffeeclone.order.model.OrderItem;
import com.example.gccoffeeclone.order.model.OrderStatus;
import com.example.gccoffeeclone.product.model.Category;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class OrderJdbcRepository implements OrderRepository {

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        var updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        return new Order(orderId, email, address, postcode, orderStatus, createdAt, updatedAt);

    };

    private static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");
        return new OrderItem(productId, category, price, quantity);
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
        insertOrderItems(order);
        return order;
    }

    @Override
    public void updateOrderAndOrderItems(Order order) {
        jdbcTemplate.update(
            "UPDATE orders SET address=:address, postcode=:postcode, updated_at=:updatedAt WHERE order_id=UUID_TO_BIN(:orderId)",
            toOrderParamMap(order));
        deleteAllOrderItems(order.getOrderId());
        insertOrderItems(order);
    }

    @Override
    public void insertOrderItems(Order order) {
        order.getOrderItems().forEach(item ->
            jdbcTemplate.update(
                "INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) "
                    +
                    "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(),
                    order.getUpdatedAt(), item)));
    }

    @Override
    public void deleteAllOrderItems(UUID orderId) {
        jdbcTemplate.update(
            "DELETE FROM order_items WHERE order_id=UUID_TO_BIN(:orderId)",
            Map.of("orderId", orderId.toString().getBytes()));
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
    public Optional<Order> findAcceptedOrderByEmail(String email, OrderStatus orderStatus) {
        var orders = jdbcTemplate
            .query("SELECT * FROM orders WHERE email = :email AND order_status = :orderStatus",
                Map.of(
                    "email", email,
                    "orderStatus", orderStatus.name()),
                orderRowMapper);
        if (orders.isEmpty()) {
            return Optional.empty();
        }

        var order = DataAccessUtils.singleResult(orders);
        List<OrderItem> orderItems = jdbcTemplate.query(
            "SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
            toOrderParamMap(order),
            orderItemRowMapper);
        order.setOrderItems(orderItems);
        return Optional.ofNullable(order);
    }

    @Override
    public void updateOrderStatus(List<byte[]> orderIds, OrderStatus orderStatus) {

        SqlParameterSource parameters = new MapSqlParameterSource("orderIds", orderIds)
            .addValue("orderStatus", orderStatus.name());
        var update = jdbcTemplate.update(
            "UPDATE orders SET order_status = :orderStatus WHERE BIN_TO_UUID(order_id) IN (:orderIds)",
            parameters);
        if (update > 0) {
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


