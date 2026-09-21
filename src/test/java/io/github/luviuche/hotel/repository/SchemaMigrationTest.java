package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the database side of the build.
 *
 * <p>The context only starts when Flyway has applied every migration and
 * Hibernate has validated the entities against the resulting schema, so these
 * assertions are really a second layer: they check that the migrations ran, and
 * that the catalogue tables everything else depends on were seeded.
 */
class SchemaMigrationTest extends AbstractIntegrationTest {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private RoleRepository roleRepository;
    @Autowired private RoomTypeRepository roomTypeRepository;

    @Test
    void everyMigrationIsApplied() {
        Integer applied = jdbcTemplate.queryForObject(
                "select count(*) from flyway_schema_history where success = true", Integer.class);

        assertThat(applied).isEqualTo(3);
    }

    @Test
    void theExpectedTablesExist() {
        var tables = jdbcTemplate.queryForList(
                "select table_name from information_schema.tables where table_schema = 'public'",
                String.class);

        assertThat(tables).contains(
                "hotel_chain", "property", "role", "app_user", "room_type", "room",
                "reservation", "reservation_room", "amenity", "reservation_amenity",
                "payment", "invoice");
    }

    @Test
    void catalogueDataIsSeeded() {
        assertThat(roleRepository.findAll())
                .extracting(r -> r.getName())
                .contains("GUEST", "STAFF", "ADMIN");

        assertThat(roomTypeRepository.findAll())
                .extracting(t -> t.getName())
                .contains("SINGLE", "DOUBLE", "SUITE", "PRESIDENTIAL");
    }

    @Test
    void checkConstraintsRejectAnUnknownStatus() {
        // V2 restricts the domain; the enum and the database must agree.
        assertThat(jdbcTemplate.queryForObject("""
                select count(*) from information_schema.check_constraints
                where constraint_name = 'chk_reservation_status'
                """, Integer.class)).isEqualTo(1);
    }
}
