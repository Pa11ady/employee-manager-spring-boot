package ru.practicum.employeemanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.employeemanager.dto.*;
import ru.practicum.employeemanager.model.Status;
import ru.practicum.employeemanager.service.OrderItemService;
import ru.practicum.employeemanager.service.OrderService;

import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PostMapping
    public OrderResponse create(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Добавление заказа {}", orderRequest);
        return orderService.create(orderRequest);
    }

    @GetMapping("/{orderId}")
    public OrderFullResponse get(@PathVariable long orderId) {
        log.info("Получение заказа по id {}", orderId);
        return orderService.findById(orderId);
    }

    @GetMapping
    public Page<OrderResponse> getAll(
            @RequestParam(required = false) Status status, @RequestParam(required = false) Instant createdAt,
            @RequestParam(required = false) Long productId,
            @PageableDefault(sort = "createdAt") Pageable pageable) {
        log.info("Получение всех заказов");
        return orderService.findAll(status, createdAt, productId, pageable);
    }

    @PutMapping("/{orderId}")
    public OrderResponse update(@PathVariable long orderId,
                                @Valid @RequestBody UpdateOrderRequest orderRequest) {
        log.info("Обновление заказа {} {}", orderId, orderRequest);
        return orderService.update(orderId, orderRequest);
    }

    @DeleteMapping("/{orderId}")
    public void delete(@PathVariable long orderId) {
        log.info("Удаление заказа по id {}", orderId);
        orderService.delete(orderId);
    }

    @PostMapping("/{orderId}/items")
    public OrderItemResponse addItem(@PathVariable long orderId,
                                     @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Добавление товара {} {}", orderId, orderItemRequest);
        return orderItemService.create(orderId, orderItemRequest);
    }

    @PutMapping("/{orderId}/items/{itemId}")
    public OrderItemResponse updateItem(@PathVariable long orderId, @PathVariable long itemId,
                                        @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Обновление товара {} {}", itemId, orderItemRequest);
        return orderItemService.update(orderId, itemId, orderItemRequest);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public void deleteItem(@PathVariable long orderId, @PathVariable long itemId) {
        log.info("Удаление товара {} из заказа  {}", itemId, orderId);
        orderItemService.delete(orderId, itemId);
    }
}
