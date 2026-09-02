package ru.practicum.employeemanager.dto;

import java.util.List;
import java.util.Objects;

public record CustomerWithOrdersResponse(Long id, String name, String surname, String email, String phone,
                                         List<OrderShortResponse> orders) {
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
