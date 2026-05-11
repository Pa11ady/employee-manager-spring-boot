package ru.practicum.employeemanager.repository;

import ru.practicum.employeemanager.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee create(Employee employee);

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    Employee update(Employee employee);

    void delete(Long id);

    Optional<Long> findIdByEmail(String email);
}
