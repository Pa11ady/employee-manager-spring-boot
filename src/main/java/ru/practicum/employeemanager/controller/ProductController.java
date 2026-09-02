package ru.practicum.employeemanager.controller;

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
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Добавление товара {}", productRequest);
        return productService.create(productRequest);
    }

    @GetMapping("/{productId}")
    public ProductResponse get(@PathVariable Long productId) {
        log.info("Получение товара по id {}", productId);
        return productService.findById(productId);
    }

    @GetMapping()
    public Page<ProductResponse> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(sort = {"name", "price"}) Pageable pageable) {
        log.info("Получение всех товаров");
        return productService.findAll(name, minPrice, maxPrice, pageable);
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable Long productId, @Valid @RequestBody ProductRequest productRequest) {
        log.info("Обновление товара {} {}", productId, productRequest);
        return productService.update(productId, productRequest);
    }

    @DeleteMapping("/{productId}")
    public void delete(@PathVariable Long productId) {
        log.info("Удаление товара по id {}", productId);
        productService.delete(productId);
    }
}
