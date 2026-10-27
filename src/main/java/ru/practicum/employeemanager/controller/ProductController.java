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
import ru.practicum.employeemanager.dto.ProductRequest;
import ru.practicum.employeemanager.dto.ProductResponse;
import ru.practicum.employeemanager.service.ProductService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "Товары", description = "Операции с товарами")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Создать новый товар")
    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Создание нового товара");
        log.debug("Данные запроса на создание товара: {}", productRequest);
        return productService.create(productRequest);
    }

    @Operation(summary = "Получить товар по ID", description = "Возвращает данные товара, если он найден")
    @GetMapping("/{productId}")
    public ProductResponse get(
            @Parameter(description = "Уникальный идентификатор товара", example = "1")
            @PathVariable Long productId) {
        log.info("Получение товара по ID: {}", productId);
        return productService.findById(productId);
    }

    @Operation(summary = "Получить список товаров", description = "Возвращает страницу товаров с поддержкой фильтрации по цене, названию и пагинации")
    @GetMapping
    public Page<ProductResponse> getAll(
            @Parameter(description = "Фильтр по названию товара")
            @RequestParam(required = false) String name,

            @Parameter(description = "Минимальная цена товара")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Максимальная цена товара")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Параметры пагинации и сортировки (page, size, sort)")
            @PageableDefault(sort = {"name", "price"}) Pageable pageable) {
        log.info("Получение списка товаров");
        log.debug("Фильтры: name='{}', minPrice={}, maxPrice={}, pageable={}",
                name, minPrice, maxPrice, pageable);
        return productService.findAll(name, minPrice, maxPrice, pageable);
    }

    @Operation(summary = "Обновление товара")
    @PutMapping("/{productId}")
    public ProductResponse update(
            @Parameter(description = "Уникальный идентификатор товара", example = "1")
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest) {
        log.info("Обновление товара ID={}", productId);
        log.debug("Данные запроса на обновление товара: {}", productRequest);
        return productService.update(productId, productRequest);
    }

    @Operation(summary = "Удаление товара по ID")
    @DeleteMapping("/{productId}")
    public void delete(
            @Parameter(description = "Уникальный идентификатор товара", example = "1")
            @PathVariable Long productId) {
        log.info("Удаление товара по ID: {}", productId);
        productService.delete(productId);
    }
}
