package ru.practicum.employeemanager.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.employeemanager.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmployeeRepositoryMemory implements EmployeeRepository {
    private final Map<Long, Employee> employees = new HashMap<>();
    private long ids = 1;

    @Override
    public Employee create(Employee employee) {
        employee.setId(ids++);
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return List.copyOf(employees.values());
    }

    @Override
    public Employee update(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public void delete(Long id) {
        employees.remove(id);
    }

    @Override
    public Optional<Long> findIdByEmail(String email) {
        return employees.entrySet()
                .stream()
                .filter(e -> email.equalsIgnoreCase(e.getValue().getEmail()))
                .map(Map.Entry::getKey)
                .findAny();
    }
}
