package com.upc.matchpoint.shared.infrastructure.persistence.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigrationRunner.class);
    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("Verifying and updating database schema for TEXT columns...");
            
            // For PostgreSQL, change the type of image_url to TEXT
            // We use IF EXISTS to avoid errors if the table doesn't exist yet (first run)
            jdbcTemplate.execute("ALTER TABLE IF EXISTS coaches ALTER COLUMN image_url TYPE TEXT;");
            logger.info("Successfully updated coaches table.");

            jdbcTemplate.execute("ALTER TABLE IF EXISTS courts ALTER COLUMN image_url TYPE TEXT;");
            logger.info("Successfully updated courts table.");
            
        } catch (Exception e) {
            // If it's a local H2 or MySQL database during tests, this PostgreSQL syntax might fail,
            // so we catch the exception and log a warning to avoid crashing the application.
            logger.warn("Could not alter column types. This is expected if running on a non-PostgreSQL database or if the tables are not yet created: " + e.getMessage());
        }
    }
}
