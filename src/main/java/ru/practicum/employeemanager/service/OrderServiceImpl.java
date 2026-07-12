package ru.practicum.employeemanager.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.employeemanager.dto.OrderFullResponse;
import ru.practicum.employeemanager.dto.OrderRequest;
import ru.practicum.employeemanager.dto.OrderResponse;
import ru.practicum.employeemanager.dto.UpdateOrderRequest;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.mapper.OrderMapper;
import ru.practicum.employeemanager.model.*;
import ru.practicum.employeemanager.repository.CustomerRepository;
import ru.practicum.employeemanager.repository.OrderRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        Customer customer = getCustomer(orderRequest.customerId());
        Order order = orderMapper.toEntity(orderRequest);
        order.setCustomer(customer);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    private Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Клиент не найден: " + customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderFullResponse findById(long id) {
        Order order = orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден: " + id));
        return orderMapper.toResponseFull(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> findAll(Status status, Instant createdAt, Long productId, Pageable pageable) {
        Specification<Order> spec = Specification.unrestricted();

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (createdAt != null) {
            // Полуоткрытый интервал [start, end) - включая start, исключая end
            Instant start = createdAt.truncatedTo(ChronoUnit.SECONDS);
            Instant end = start.plusSeconds(1);
            spec = spec.and((root, query, cb) ->
                    cb.and(
                            cb.greaterThanOrEqualTo(root.get("createdAt"), start),
                            cb.lessThan(root.get("createdAt"), end)
                    )
            );
        }

        if (productId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Order, OrderItem> itemJoin = root.join("items", JoinType.INNER);
                Join<OrderItem, Product> productJoin = itemJoin.join("product", JoinType.INNER);
                if (query != null) {
                    query.distinct(true);
                }
                return cb.equal(productJoin.get("id"), productId);
            });
        }

        Page<Order> page = orderRepository.findAll(spec, pageable);
        return page.map(orderMapper::toResponse);
    }

    @Override
    @Transactional
    public OrderResponse update(long id, UpdateOrderRequest orderRequest) {
        Order order = getOrder(id);
        orderMapper.updateEntityFromRequest(orderRequest, order);
        return orderMapper.toResponse(order);
    }

    private Order getOrder(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден: " + id));
    }

    @Override
    @Transactional
    public void delete(long id) {
        orderRepository.delete(getOrder(id));
    }
}
