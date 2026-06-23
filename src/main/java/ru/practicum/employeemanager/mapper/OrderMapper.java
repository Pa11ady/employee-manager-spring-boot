package ru.practicum.employeemanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.employeemanager.dto.OrderRequest;
import ru.practicum.employeemanager.dto.OrderResponse;
import ru.practicum.employeemanager.dto.OrderWithCustomerResponse;
import ru.practicum.employeemanager.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Order toEntity(OrderRequest request);

    @Mapping(target = "customerId", source = "customer.id")
    OrderResponse toResponse(Order order);

    OrderWithCustomerResponse toResponseFull(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(OrderRequest request, @MappingTarget Order order);
}