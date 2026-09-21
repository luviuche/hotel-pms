package io.github.luviuche.hotel;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base class for every integration test.
 *
 * <p>All subclasses share the same Spring context, which means a single
 * PostgreSQL container is started once and reused for the whole suite. Flyway
 * builds the schema on it and Hibernate validates the entities against the
 * result, so a migration that drifts from the entities fails the build.
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresContainerConfiguration.class)
public abstract class AbstractIntegrationTest {
}
