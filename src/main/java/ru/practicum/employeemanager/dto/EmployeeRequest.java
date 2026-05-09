package ru.practicum.employeemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.practicum.employeemanager.model.Role;

public record EmployeeRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String name,

        @NotBlank
        @Size(min = 2, max = 100)
        String surname,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(min = 6, max = 64)
        String password,

        @NotNull
        Role role
) {
}
