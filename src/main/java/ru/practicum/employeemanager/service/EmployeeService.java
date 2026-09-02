package ru.practicum.employeemanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest employeeRequest);

    EmployeeResponse findById(long id);

    Page<EmployeeResponse> findAll(String name, String surname, String email, String phone, Pageable pageable);

    EmployeeResponse update(long id, EmployeeRequest employeeRequest);

    void delete(long id);
}
