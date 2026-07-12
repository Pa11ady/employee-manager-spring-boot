package ru.practicum.employeemanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.employeemanager.dto.OrderFullResponse;
import ru.practicum.employeemanager.dto.OrderRequest;
import ru.practicum.employeemanager.dto.OrderResponse;
import ru.practicum.employeemanager.dto.UpdateOrderRequest;
import ru.practicum.employeemanager.model.Order;

@Mapper(componentModel = "spring",
        uses = {CustomerMapper.class, OrderItemMapper.class})

public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderRequest request);

    @Mapping(target = "customerId", source = "customer.id")
    OrderResponse toResponse(Order order);

    OrderFullResponse toResponseFull(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntityFromRequest(UpdateOrderRequest request, @MappingTarget Order order);
}