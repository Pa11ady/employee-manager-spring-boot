package ru.practicum.employeemanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.employeemanager.dto.ProductRequest;
import ru.practicum.employeemanager.dto.ProductResponse;

import java.math.BigDecimal;

public interface ProductService {
    ProductResponse create(ProductRequest productRequest);

    ProductResponse findById(long id);

    Page<ProductResponse> findAll(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    ProductResponse update(long id, ProductRequest productRequest);

    void delete(long id);
}
