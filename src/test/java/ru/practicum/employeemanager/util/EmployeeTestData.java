package ru.practicum.employeemanager.util;

import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.model.Employee;
import ru.practicum.employeemanager.model.Role;

public class EmployeeTestData {

    public static Employee createEmployee() {
        return new Employee(1L, "Иван", "Иванов", "ivan@example.com",
                "securePassword123", Role.MANAGER);
    }

    public static Employee createEmployeeWithId(Long id) {
        return new Employee(id, "Иван", "Иванов", "ivan@example.com",
                "securePassword123", Role.MANAGER);
    }

    public static EmployeeRequest createEmployeeRequest() {
        return new EmployeeRequest("Петр", "Петров", "petr@example.com",
                "newPassword456", Role.ADMIN);
    }
}