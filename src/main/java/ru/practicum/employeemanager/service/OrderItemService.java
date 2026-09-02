package ru.practicum.employeemanager.service;

import ru.practicum.employeemanager.dto.OrderItemRequest;
import ru.practicum.employeemanager.dto.OrderItemResponse;

public interface OrderItemService {
    OrderItemResponse create(long orderId, OrderItemRequest orderItemRequest);

    OrderItemResponse update(long orderId, long itemId, OrderItemRequest orderItemRequest);

    void delete(long orderId, long itemId);
}
