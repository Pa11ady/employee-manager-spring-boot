package ru.practicum.employeemanager.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.employeemanager.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryMemory implements EmployeeRepository {
    @Override
    public Employee create(Employee employee) {
        return employee;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Employee> findAll() {
        return List.of();
    }

    @Override
    public Employee update(Employee employee) {
        return employee;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<Long> findIdByEmail(String email) {
        return Optional.of(1L);
    }
}
