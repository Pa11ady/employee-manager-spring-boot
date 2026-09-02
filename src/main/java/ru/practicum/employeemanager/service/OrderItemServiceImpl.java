package ru.practicum.employeemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.employeemanager.dto.OrderItemRequest;
import ru.practicum.employeemanager.dto.OrderItemResponse;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.mapper.OrderItemMapper;
import ru.practicum.employeemanager.model.Order;
import ru.practicum.employeemanager.model.OrderItem;
import ru.practicum.employeemanager.model.Product;
import ru.practicum.employeemanager.repository.OrderItemRepository;
import ru.practicum.employeemanager.repository.OrderRepository;
import ru.practicum.employeemanager.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderItemResponse create(long orderId, OrderItemRequest orderItemRequest) {
        Order order = getOrder(orderId);
        Product product = getProduct(orderItemRequest.productId());
        OrderItem item = orderItemMapper.toEntity(orderItemRequest);
        item.setOrder(order);
        item.setProduct(product);
        return orderItemMapper.toResponse(orderItemRepository.save(item));
    }

    @Override
    @Transactional
    public OrderItemResponse update(long orderId, long itemId, OrderItemRequest orderItemRequest) {
        OrderItem item = getItem(orderId, itemId);
        Product product = getProduct(orderItemRequest.productId());
        item.setProduct(product);
        orderItemMapper.updateEntityFromRequest(orderItemRequest, item);
        return orderItemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public void delete(long orderId, long itemId) {
        orderItemRepository.delete(getItem(orderId, itemId));
    }

    private Order getOrder(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден: " + id));
    }

    private Product getProduct(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар не найден: " + id));
    }

    private OrderItem getItem(long orderId, long itemId) {
        return orderItemRepository.findByIdAndOrder_Id(itemId, orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Товарная позиция " + itemId + " не найдена в заказе " + orderId));
    }
}
