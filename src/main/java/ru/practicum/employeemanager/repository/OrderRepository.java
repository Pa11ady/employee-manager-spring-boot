package ru.practicum.employeemanager.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.employeemanager.model.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    @Query("SELECT o FROM  Order o WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") long id);
}
