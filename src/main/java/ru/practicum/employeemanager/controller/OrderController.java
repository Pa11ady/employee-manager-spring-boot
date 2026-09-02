package ru.practicum.employeemanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Заказы", description = "Операции с заказами и позициями заказов")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Operation(summary = "Создать новый заказ")
    @PostMapping
    public OrderResponse create(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Добавление заказа {}", orderRequest);
        return orderService.create(orderRequest);
    }

    @Operation(summary = "Получить заказ по ID", description = "Возвращает полные данные заказа, если он найден")
    @GetMapping("/{orderId}")
    public OrderFullResponse get(
            @Parameter(description = "Уникальный идентификатор заказа", example = "1")
            @PathVariable long orderId) {
        log.info("Получение заказа по id {}", orderId);
        return orderService.findById(orderId);
    }

    @Operation(summary = "Получить список заказов", description = "Возвращает страницу заказов с поддержкой фильтрации и пагинации")
    @GetMapping
    public Page<OrderResponse> getAll(
            @Parameter(description = "Фильтр по статусу заказа")
            @RequestParam(required = false) Status status,

            @Parameter(description = "Фильтр по дате создания заказа")
            @RequestParam(required = false) Instant createdAt,

            @Parameter(description = "Фильтр по идентификатору продукта")
            @RequestParam(required = false) Long productId,

            @Parameter(description = "Параметры пагинации и сортировки (page, size, sort)")
            @PageableDefault(sort = "createdAt") Pageable pageable) {
        log.info("Получение всех заказов");
        return orderService.findAll(status, createdAt, productId, pageable);
    }

    @Operation(summary = "Обновление заказа")
    @PutMapping("/{orderId}")
    public OrderResponse update(
            @Parameter(description = "Уникальный идентификатор заказа", example = "1")
            @PathVariable long orderId,
            @Valid @RequestBody UpdateOrderRequest orderRequest) {
        log.info("Обновление заказа {} {}", orderId, orderRequest);
        return orderService.update(orderId, orderRequest);
    }

    @Operation(summary = "Удаление заказа по ID")
    @DeleteMapping("/{orderId}")
    public void delete(
            @Parameter(description = "Уникальный идентификатор заказа", example = "1")
            @PathVariable long orderId) {
        log.info("Удаление заказа по id {}", orderId);
        orderService.delete(orderId);
    }

    @Operation(summary = "Добавить позицию (товар) в заказ")
    @PostMapping("/{orderId}/items")
    public OrderItemResponse addItem(
            @Parameter(description = "Уникальный идентификатор заказа", example = "1")
            @PathVariable long orderId,
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Добавление товара {} {}", orderId, orderItemRequest);
        return orderItemService.create(orderId, orderItemRequest);
    }

    @Operation(summary = "Обновить позицию (товар) в заказе")
    @PutMapping("/{orderId}/items/{itemId}")
    public OrderItemResponse updateItem(
            @Parameter(description = "Уникальный идентификатор заказа", example = "1")
            @PathVariable long orderId,
            @Parameter(description = "Уникальный идентификатор позиции (товара) в заказе", example = "10")
            @PathVariable long itemId,
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Обновление товара {} {}", itemId, orderItemRequest);
        return orderItemService.update(orderId, itemId, orderItemRequest);
    }

    @Operation(summary = "Удалить позицию (товар) из заказа")
    @DeleteMapping("/{orderId}/items/{itemId}")
    public void deleteItem(
            @Parameter(description = "Уникальный идентификатор заказа", example = "1")
            @PathVariable long orderId,
            @Parameter(description = "Уникальный идентификатор позиции (товара) в заказе", example = "10")
            @PathVariable long itemId) {
        log.info("Удаление товара {} из заказа {}", itemId, orderId);
        orderItemService.delete(orderId, itemId);
    }
}