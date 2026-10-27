package ru.practicum.employeemanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRequest request);

    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(EmployeeRequest request, @MappingTarget Employee employee);

}