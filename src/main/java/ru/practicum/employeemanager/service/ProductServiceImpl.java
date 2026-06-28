package ru.practicum.employeemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.employeemanager.dto.ProductRequest;
import ru.practicum.employeemanager.dto.ProductResponse;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.mapper.ProductMapper;
import ru.practicum.employeemanager.model.Product;
import ru.practicum.employeemanager.repository.ProductRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        //Нарушение уникальности имени будет глобальным обработчиком
        Product product = productRepository.save(productMapper.toEntity(productRequest));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse findById(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар не найден: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> findAll(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<Product> spec = Specification.unrestricted();
        if (StringUtils.hasText(name)) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        return productRepository.findAll(spec, pageable).map(productMapper::toResponse);
    }

    @Override
    public ProductResponse update(long id, ProductRequest productRequest) {
        Product product = getProduct(id);
        productMapper.updateEntityFromRequest(productRequest, product);
        return productMapper.toResponse(product);
    }

    @Override
    public void delete(long id) {
        productRepository.delete(getProduct(id));
    }

    private Product getProduct(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар не найден: " + id));
    }
}
