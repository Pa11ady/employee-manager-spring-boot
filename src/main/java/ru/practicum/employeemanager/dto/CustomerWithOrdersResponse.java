package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Objects;

@Schema(description = "Полная информация о клиенте вместе с его заказами")
public record CustomerWithOrdersResponse(
        @Schema(description = "Уникальный идентификатор клиента", example = "123")
        Long id,

        @Schema(description = "Имя клиента", example = "Иван")
        String name,

        @Schema(description = "Фамилия клиента", example = "Иванов")
        String surname,

        @Schema(description = "Электронная почта клиента", example = "ivan@example.com")
        String email,

        @Schema(description = "Номер телефона клиента", example = "+79991234567")
        String phone,

        @Schema(description = "Список заказов клиента")
        List<OrderShortResponse> orders
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CustomerWithOrdersResponse that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
