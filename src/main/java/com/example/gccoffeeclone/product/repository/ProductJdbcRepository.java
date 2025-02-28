package com.example.gccoffeeclone.product.repository;

import static com.example.gccoffeeclone.utils.JDBCUtils.*;

import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.model.Product;
import java.util.*;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class ProductJdbcRepository implements ProductRepository {

    private static final RowMapper<Product> productRowMapper = ((resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(
            productId,
            productName,
            category,
            price,
            description,
            createdAt,
            updatedAt
        );
    });

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM products",
            productRowMapper
        );
    }

    @Override
    public void insert(Product product) {
        jdbcTemplate.update(
            "INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)"
                +
                " VALUES (UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)",
            toParamMap(product));
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(
            "UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, created_at = :createdAt, updated_at = :updatedAt"
                +
                " WHERE product_id = UUID_TO_BIN(:productId)",
            toParamMap(product)
        );
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        var product = jdbcTemplate.query(
            "SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId)",
            Collections.singletonMap("productId", productId.toString().getBytes()),
            productRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(product));
    }

    @Override
    public Optional<Product> findByName(String productName) {
        var product = jdbcTemplate.query(
            "SELECT * FROM products WHERE product_name = :productName",
            Collections.singletonMap("productName", productName),
            productRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(product));
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query(
            "SELECT * FROM products WHERE category = :category",
            Collections.singletonMap("category", category.toString()),
            productRowMapper
        );
    }

    @Override
    public void deleteById(UUID productId) {
        jdbcTemplate.update(
            "DELETE FROM products where product_id = UUID_TO_BIN(:productId)",
            Collections.singletonMap("productId", productId.toString().getBytes()));
    }

    private Map<String, Object> toParamMap(Product product) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updatedAt", product.getUpdatedAt());
        return paramMap;
    }

}