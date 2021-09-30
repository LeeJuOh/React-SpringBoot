package com.example.gccoffeeclone.order.controller.api;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.gccoffeeclone.order.controller.OrderDto;
import com.example.gccoffeeclone.order.controller.OrderItemDto;
import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.model.Product;
import com.example.gccoffeeclone.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderRestControllerTest {

    static EmbeddedMysql embeddedMysql;
    private static Product newProduct;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;


    @BeforeAll
    static void setup() {
        var config = aMysqldConfig(v8_0_11)
            .withCharset(Charset.UTF8)
            .withPort(2215)
            .withUser("test", "test1234!")
            .withTimeZone("Asia/Seoul")
            .build();
        embeddedMysql = anEmbeddedMysql(config)
            .addSchema("test-gc_coffee", ScriptResolver.classPathScript("schema.sql"))
            .start();

        var productId = UUID.randomUUID();
        var productName = "과테말라";
        var category = Category.COFFEE_BEAN_PACKAGE;
        var price = 15000L;
        var description = "프리미엄 원두입니다.";

        newProduct =  new Product(productId, productName, category, price, description,
            LocalDateTime.now(), LocalDateTime.now());
    }

    @AfterAll
    static void cleanup() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("주문 요청 API")
    @Order(1)
    void createOrder() throws Exception {
        productRepository.insert(newProduct);

        var orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(newProduct.getProductId());
        orderItemDto.setCategory(newProduct.getCategory());
        orderItemDto.setPrice(newProduct.getPrice());
        orderItemDto.setQuantity(3);

        OrderDto orderDto = new OrderDto();
        orderDto.setEmail("tester@email.com");
        orderDto.setAddress("경기도 구리시");
        orderDto.setPostcode("12345");
        orderDto.setOrderItems(List.of(orderItemDto));

        mockMvc.perform(post("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(orderDto)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

}