package ru.practicum.employeemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.model.Role;
import ru.practicum.employeemanager.service.EmployeeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@DisplayName("EmployeeController")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeRequest validRequest;
    private EmployeeResponse expectedResponse;
    private final long employeeId = 123L;

    @BeforeEach
    void setUp() {
        validRequest = new EmployeeRequest(
                "Петр", "Петров", "petr@company.com", "+79991234567", "StrongP@ssw0rd123",
                Role.MANAGER
        );

        expectedResponse = new EmployeeResponse(
                employeeId, "Петр", "Петров", "petr@company.com", "+79991234567", Role.MANAGER
        );
    }

    @Test
    @DisplayName("Должен вернуть 200 OK и данные сотрудника при валидном запросе")
    void shouldReturn200AndEmployeeResponseWhenCreateIsValid() throws Exception {
        when(employeeService.create(any(EmployeeRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(employeeService, times(1)).create(any(EmployeeRequest.class));
    }

    @Test
    @DisplayName("Должен вернуть 400 Bad Request при отсутствии обязательного поля")
    void shouldReturn400WhenCreateWithMissingRequiredField() throws Exception {
        String invalidJson = """
                {
                  "surname": "Петров",
                  "email": "petr@company.com",
                  "password": "StrongP@ssw0rd123",
                  "role": "MANAGER"
                }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).create(any());
    }

    @Test
    @DisplayName("Должен вернуть 200 OK и данные сотрудника, если он найден")
    void shouldReturn200AndEmployeeResponseWhenGetByIdAndFound() throws Exception {
        when(employeeService.findById(employeeId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/employees/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(employeeService).findById(employeeId);
    }

    @Test
    @DisplayName("Должен вернуть 404 Not Found, если сотрудник не найден")
    void shouldReturn404WhenGetByIdAndNotFound() throws Exception {
        when(employeeService.findById(employeeId))
                .thenThrow(new NotFoundException(("Employee not found")));

        mockMvc.perform(get("/employees/{employeeId}", employeeId))
                .andExpect(status().isNotFound());

        verify(employeeService).findById(employeeId);
    }

    @Test
    @DisplayName("Должен вернуть 200 OK и страницу сотрудников без фильтров")
    void shouldReturn200AndPageOfEmployeesWhenGetAllWithoutFilters() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EmployeeResponse> page = new PageImpl<>(List.of(expectedResponse), pageable, 1);

        when(employeeService.findAll(isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/employees")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(employeeId))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(employeeService).findAll(isNull(), isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    @DisplayName("Должен вернуть 200 OK и отфильтрованный список при передаче параметров")
    void shouldReturn200AndFilteredPageWhenGetAllWithParams() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EmployeeResponse> page = new PageImpl<>(List.of(expectedResponse), pageable, 1);

        when(employeeService.findAll(eq("Петр"), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/employees")
                        .param("name", "Петр")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(employeeService).findAll(eq("Петр"), isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    @DisplayName("Должен вернуть 200 OK и обновленные данные при валидном запросе")
    void shouldReturn200AndUpdatedResponseWhenUpdateIsValid() throws Exception {
        when(employeeService.update(eq(employeeId), any(EmployeeRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(put("/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(employeeService).update(eq(employeeId), any(EmployeeRequest.class));
    }

    @Test
    @DisplayName("Должен вернуть 400 Bad Request при невалидном email")
    void shouldReturn400WhenUpdateWithInvalidEmail() throws Exception {
        String invalidJson = """
                {
                  "name": "Петр",
                  "surname": "Петров",
                  "email": "invalid-email-without-at",
                  "password": "StrongP@ssw0rd123",
                  "role": "MANAGER"
                }
                """;

        mockMvc.perform(put("/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).update(anyLong(), any());
    }

    @Test
    @DisplayName("Должен вернуть 200 OK при успешном удалении")
    void shouldReturn200WhenDeleteSuccessfully() throws Exception {
        doNothing().when(employeeService).delete(employeeId);

        mockMvc.perform(delete("/employees/{employeeId}", employeeId))
                .andExpect(status().isOk());

        verify(employeeService).delete(employeeId);
    }

    @Test
    @DisplayName("Должен вернуть 404 Not Found, если пытаемся удалить несуществующего сотрудника")
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        doThrow(new NotFoundException("Not found")).when(employeeService).delete(employeeId);

        mockMvc.perform(delete("/employees/{employeeId}", employeeId))
                .andExpect(status().isNotFound());

        verify(employeeService).delete(employeeId);
    }
}
