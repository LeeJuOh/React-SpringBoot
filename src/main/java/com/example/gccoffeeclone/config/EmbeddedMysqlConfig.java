package com.example.gccoffeeclone.config;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


@Configuration
@Profile("local")
public class EmbeddedMysqlConfig {

    private EmbeddedMysql embeddedMysql;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:3307/local-gc_coffee")
            .username("local").password("local1234!").type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @PostConstruct
    void setUp() {
        MysqldConfig config = aMysqldConfig(v8_0_11)
            .withCharset(UTF8)
            .withPort(3307
            )
            .withUser("local", "local1234!")
            .withTimeZone("Asia/Seoul")
            .withTimeout(2, TimeUnit.MINUTES)
            .build();

        embeddedMysql = anEmbeddedMysql(config)
            .addSchema("local-gc_coffee", classPathScript("schema.sql"))
            .start();
    }

    @PreDestroy
    void cleanUp() {
        embeddedMysql.stop();
    }

}
