package ru.practicum.employeemanager.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertEquals(employee.getPhone(), response.phone());
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
        assertEquals(request.phone(), entity.getPhone()); // Проверка маппинга телефона
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
        assertEquals(request.phone(), existingEmployee.getPhone()); // Проверка маппинга телефона при обновлении
        assertEquals(request.password(), existingEmployee.getPassword());
        assertEquals(request.role(), existingEmployee.getRole());
    }
}
