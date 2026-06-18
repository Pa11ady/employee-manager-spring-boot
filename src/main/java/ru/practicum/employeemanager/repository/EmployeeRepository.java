package ru.practicum.employeemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.employeemanager.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Long> findIdByEmail(String email);
    boolean existsByEmail(String email);
}
