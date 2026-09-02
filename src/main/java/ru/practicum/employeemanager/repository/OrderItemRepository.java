package ru.practicum.employeemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.employeemanager.model.OrderItem;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrder_Id(long itemId, long orderId);
}
