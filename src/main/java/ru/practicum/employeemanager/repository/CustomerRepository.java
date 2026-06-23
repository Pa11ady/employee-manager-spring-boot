package ru.practicum.employeemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.employeemanager.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    //Optional<Long> findCustomerIdByEmail(String email); то работает то нет, магия кеша

    @Query("SELECT c.id from Customer c WHERE  c.email = :email")
    Optional<Long> findIdByEmail(@Param(("email")) String email);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.id = :id")
    Optional<Customer> findByIdWithOrders(@Param("id") Long id);
}
