package ru.practicum.employeemanager.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Стратегия для dev-окружения: перед применением миграций
 * полностью очищает схему БД.
 * Работает только если в application-dev.yml указано:
 *   spring.flyway.clean-disabled: false
 */
@Slf4j
@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevFlywayCleanStrategy {

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            log.info("****Очищаем базу данных...");
            flyway.clean();

            log.info("****[DEV] Применяем миграции...");
            flyway.migrate();

            log.info("****[DEV] База данных готова!");
        };
    }
}
