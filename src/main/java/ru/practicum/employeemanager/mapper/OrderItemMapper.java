package ru.practicum.employeemanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.employeemanager.dto.OrderItemRequest;
import ru.practicum.employeemanager.dto.OrderItemResponse;
import ru.practicum.employeemanager.dto.OrderItemSummary;
import ru.practicum.employeemanager.model.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItem toEntity(OrderItemRequest request);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    OrderItemResponse toResponse(OrderItem item);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemSummary toSummary(OrderItem item);

    List<OrderItemSummary> toSummaryList(List<OrderItem> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateEntityFromRequest(OrderItemRequest request, @MappingTarget OrderItem item);
}
