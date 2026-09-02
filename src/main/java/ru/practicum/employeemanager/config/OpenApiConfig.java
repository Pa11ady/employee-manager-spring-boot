package ru.practicum.employeemanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * <a href="http://localhost:8080/v3/api-docs">...</a>.
 */

@OpenAPIDefinition(
        info = @Info(
                title = "API моего учебного проекта",
                description = "Документация для тестирования эндпоинтов",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}