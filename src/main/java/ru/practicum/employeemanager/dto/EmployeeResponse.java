package ru.practicum.employeemanager.dto;

import ru.practicum.employeemanager.model.Role;

public record EmployeeResponse(
        Long id,
        String name,
        String surname,
        String email,
        Role role
) {
}
