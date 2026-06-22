package ru.practicum.employeemanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.employeemanager.dto.OrderRequest;
import ru.practicum.employeemanager.dto.OrderResponse;
import ru.practicum.employeemanager.dto.OrderWithCustomerResponse;
import ru.practicum.employeemanager.model.Status;
import ru.practicum.employeemanager.service.OrderService;

import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse create(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Добавление заказа {}", orderRequest);
        return orderService.create(orderRequest);
    }

    @GetMapping("/{orderId}")
    public OrderWithCustomerResponse get(@PathVariable long orderId) {
        log.info("Получение заказа по id {}", orderId);
        return orderService.findById(orderId);
    }

    @GetMapping
    public Page<OrderResponse> getAll(
            @RequestParam(required = false) Status status, @RequestParam(required = false) Instant createdAt,
            @PageableDefault(sort = "createdAt") Pageable pageable) {
        log.info("Получение всех заказов");
        return orderService.findAll(status, createdAt, pageable);
    }

    @PutMapping("/{orderId}")
    public OrderResponse update(@PathVariable long orderId,
                                @Valid @RequestBody OrderRequest orderRequest) {
        log.info("Обновление заказа {} {}", orderId, orderRequest);
        return orderService.update(orderId, orderRequest);
    }

    @DeleteMapping("/{orderId}")
    public void delete(@PathVariable long orderId) {
        log.info("Удаление заказа по id {}", orderId);
        orderService.delete(orderId);
    }
}