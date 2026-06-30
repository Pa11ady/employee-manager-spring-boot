package ru.practicum.employeemanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.employeemanager.dto.OrderFullResponse;
import ru.practicum.employeemanager.dto.OrderRequest;
import ru.practicum.employeemanager.dto.OrderResponse;
import ru.practicum.employeemanager.model.Status;

import java.time.Instant;

public interface OrderService {

    OrderResponse create(OrderRequest orderRequest);

    OrderFullResponse findById(long id);

    Page<OrderResponse> findAll(Status status, Instant createdAt, Pageable pageable);

    OrderResponse update(long id, OrderRequest orderRequest);

    void delete(long id);
}