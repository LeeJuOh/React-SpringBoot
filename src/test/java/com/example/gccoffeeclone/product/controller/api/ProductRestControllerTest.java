package com.example.gccoffeeclone.product.controller.api;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.model.Product;
import com.example.gccoffeeclone.product.repository.ProductRepository;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
class ProductRestControllerTest {

    static EmbeddedMysql embeddedMysql;

    @Autowired
    protected MockMvc mockMvc;

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
    }

    @AfterAll
    static void cleanup() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("상품 전체 조회 API 테스트")
    @Order(1)
    void getAllProducts() throws Exception {
        IntStream.range(0, 5)
            .forEach((i) -> {
                var productId = UUID.randomUUID();
                var productName = "에티오피아 원두" + i;
                var category = Category.COFFEE_BEAN_PACKAGE;
                var price = i * 10000L;
                var description = "프리미엄 원두입니다.";

                var product = new Product(productId, productName, category, price, description,
                    LocalDateTime.now(), LocalDateTime.now());
                productRepository.insert(product);
            });

        mockMvc.perform(get("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$..productId").exists())
            .andExpect(jsonPath("$..productName").exists())
            .andExpect(jsonPath("$..category").exists())
            .andExpect(jsonPath("$..description").exists())
            .andExpect(jsonPath("$..createdAt").exists())
            .andExpect(jsonPath("$..updatedAt").exists());

    }

}