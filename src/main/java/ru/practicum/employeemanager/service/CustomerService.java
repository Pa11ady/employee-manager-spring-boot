package ru.practicum.employeemanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.employeemanager.dto.CustomerRequest ;
import ru.practicum.employeemanager.dto.CustomerResponse;
import ru.practicum.employeemanager.dto.CustomerWithOrdersResponse;

public interface CustomerService {
    CustomerResponse create(CustomerRequest customerRequest );

    CustomerWithOrdersResponse findById(long id);

    Page<CustomerResponse> findAll(String name, String surname, String email, String phone, Pageable pageable);

    CustomerResponse update(long id, CustomerRequest customerRequest );

    void delete(long id);
}
