package ru.practicum.employeemanager.service;

import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest employeeRequest);

    EmployeeResponse findById(long id);

    List<EmployeeResponse> findAll();

    EmployeeResponse update(long id, EmployeeRequest employeeRequest);

    void delete(long id);
}
