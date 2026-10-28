package ru.practicum.employeemanager.util;

import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.model.Employee;
import ru.practicum.employeemanager.model.Role;

public class EmployeeTestData {

    public static final String VALID_NAME = "Иван";
    public static final String VALID_SURNAME = "Иванов";
    public static final String VALID_EMAIL = "ivan.ivanov@company.com";
    public static final String VALID_PHONE = "+79991234567";
    public static final String VALID_PASSWORD = "StrongPass123";
    public static final Role VALID_ROLE = Role.MANAGER;

    public static EmployeeRequest createEmployeeRequest() {
        return new EmployeeRequest(
                VALID_NAME,
                VALID_SURNAME,
                VALID_EMAIL,
                VALID_PHONE,
                VALID_PASSWORD,
                VALID_ROLE
        );
    }

    public static Employee createEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName(VALID_NAME);
        employee.setSurname(VALID_SURNAME);
        employee.setEmail(VALID_EMAIL);
        employee.setPhone(VALID_PHONE);
        employee.setPassword(VALID_PASSWORD);
        employee.setRole(VALID_ROLE);
        return employee;
    }

    public static Employee createEmployeeWithId(Long id) {
        Employee employee = createEmployee();
        employee.setId(id);
        return employee;
    }
}
