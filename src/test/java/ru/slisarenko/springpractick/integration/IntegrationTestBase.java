package ru.slisarenko.springpractick.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.slisarenko.springpractick.integration.annotation.MyIntegrationTest;

@MyIntegrationTest
/*@Sql({
  "classpath:sql/insert_test_data.sql"
})*/
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16");

    @BeforeAll
    public static void startContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        var url = container.getJdbcUrl();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("jakarta.persistence.jdbc.url", container::getJdbcUrl);
    }
}
