package ru.practicum.employeemanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.model.Employee;
import ru.practicum.employeemanager.util.EmployeeTestData;

class EmployeeMapperTest {

    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Test
    void toResponse_shouldMapAllFields() {
        // given
        Employee employee = EmployeeTestData.createEmployee();

        // when
        EmployeeResponse response = employeeMapper.toResponse(employee);

        // then
        assertNotNull(response);
        assertEquals(employee.getId(), response.id());
        assertEquals(employee.getName(), response.name());
        assertEquals(employee.getSurname(), response.surname());
        assertEquals(employee.getEmail(), response.email());
        assertEquals(employee.getRole(), response.role());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        // given
        EmployeeRequest request = EmployeeTestData.createEmployeeRequest();

        // when
        Employee entity = employeeMapper.toEntity(request);

        // then
        assertNotNull(entity);
        assertEquals(request.name(), entity.getName());
        assertEquals(request.surname(), entity.getSurname());
        assertEquals(request.email(), entity.getEmail());
        assertEquals(request.password(), entity.getPassword());
        assertEquals(request.role(), entity.getRole());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateFieldsAndIgnoreId() {
        // given
        Employee existingEmployee = EmployeeTestData.createEmployeeWithId(99L);
        EmployeeRequest request = EmployeeTestData.createEmployeeRequest();

        // when
        employeeMapper.updateEntityFromRequest(request, existingEmployee);

        // then
        // ID должен остаться 99L
        assertEquals(99L, existingEmployee.getId());
        assertEquals(request.name(), existingEmployee.getName());
        assertEquals(request.surname(), existingEmployee.getSurname());
        assertEquals(request.email(), existingEmployee.getEmail());
        assertEquals(request.password(), existingEmployee.getPassword());
        assertEquals(request.role(), existingEmployee.getRole());
    }

    @Test
    void toResponseList_shouldMapAllFields() {
        // given
        List<Employee> employees = List.of(
                EmployeeTestData.createEmployeeWithId(1L),
                EmployeeTestData.createEmployeeWithId(2L)
        );

        // when
        List<EmployeeResponse> responses = employeeMapper.toResponseList(employees);

        // then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).id());
        assertEquals(2L, responses.get(1).id());
    }
}