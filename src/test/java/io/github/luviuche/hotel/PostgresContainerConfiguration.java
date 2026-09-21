package io.github.luviuche.hotel;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Starts a real PostgreSQL instance for the test suite. @ServiceConnection wires
 * the container's URL, user and password into the datasource, so no JDBC
 * settings are hard coded anywhere.
 *
 * <p>The image tag is pinned on purpose: tests should run against the same major
 * version the application is deployed on.
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainerConfiguration {

    private static final String POSTGRES_IMAGE = "postgres:18-alpine";

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer(POSTGRES_IMAGE);
    }
}
