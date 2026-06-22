package ru.practicum.employeemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.employeemanager.dto.OrderRequest;
import ru.practicum.employeemanager.dto.OrderResponse;
import ru.practicum.employeemanager.dto.OrderWithCustomerResponse;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.mapper.OrderMapper;
import ru.practicum.employeemanager.model.Customer;
import ru.practicum.employeemanager.model.Order;
import ru.practicum.employeemanager.model.Status;
import ru.practicum.employeemanager.repository.CustomerRepository;
import ru.practicum.employeemanager.repository.OrderRepository;

import java.time.Instant;

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
    public OrderWithCustomerResponse findById(long id) {
        Order order = orderRepository.findByIdWithCustomer(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден: " + id));
        return orderMapper.toResponseFull(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> findAll(Status status, Instant createdAt, Pageable pageable) {
        Specification<Order> spec = Specification.unrestricted();
        spec = addLike(spec, "status", status != null ? status.name() : null);
        spec = addLike(spec, "createdAt", createdAt != null ? createdAt.toString() : null);
        Page<Order> page = orderRepository.findAll(spec, pageable);
        return page.map(orderMapper::toResponse);
    }

    private Specification<Order> addLike(Specification<Order> spec, String field, String value) {
        if (StringUtils.hasText(value)) {
            return spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%")
            );
        }
        return spec;
    }

    @Override
    @Transactional
    public OrderResponse update(long id, OrderRequest orderRequest) {
        Order order = getOrder(id);
        Customer customer = getCustomer(orderRequest.customerId());
        order.setCustomer(customer);
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
