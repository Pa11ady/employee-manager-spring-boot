package ru.practicum.employeemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.model.Role;
import ru.practicum.employeemanager.repository.EmployeeRepository;
import ru.practicum.employeemanager.service.EmployeeService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@Transactional
@DisplayName("Интеграционные тесты EmployeeController (HTTP + БД)")
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeRequest validRequest;
    private String validRequestJson;

    @BeforeEach
    void setUp() throws Exception {
        validRequest = new EmployeeRequest(
                "Иван", "Петров", "ivan.petrov@example.com", "+79991234567", "securePass123", Role.MANAGER
        );
        validRequestJson = objectMapper.writeValueAsString(validRequest);
    }

    @Test
    @DisplayName("POST /employees: должен создать сотрудника и вернуть 200 OK")
    void create_whenValidRequest_shouldSaveToDatabaseAndReturnOk() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("ivan.petrov@example.com"));

        assertThat(employeeRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("POST /employees: должен вернуть 400 Bad Request при невалидных данных")
    void create_whenInvalidRequest_shouldReturnBadRequest() throws Exception {
        String invalidJson = """
                {
                  "surname": "Петров",
                  "email": "invalid-email",
                  "password": "123",
                  "role": "MANAGER"
                }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        assertThat(employeeRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("POST /employees: должен вернуть 409 Conflict при дубликате email")
    void create_whenEmailAlreadyExists_shouldReturnConflict() throws Exception {
        employeeService.create(validRequest);

        EmployeeRequest duplicateRequest = new EmployeeRequest(
                "Джон", "Доу", "IVAN.PETROV@example.com", "111", "pass123456", Role.ADMIN
        );

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());

        assertThat(employeeRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("GET /employees/{id}: должен вернуть 200 OK и данные сотрудника")
    void getById_whenExists_shouldReturnEmployee() throws Exception {
        EmployeeResponse saved = employeeService.create(validRequest);

        mockMvc.perform(get("/employees/{id}", saved.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.id()))
                .andExpect(jsonPath("$.name").value("Иван"));
    }

    @Test
    @DisplayName("GET /employees/{id}: должен вернуть 404 Not Found, если сотрудника нет")
    void getById_whenNotExists_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/employees/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /employees: должен вернуть отфильтрованную страницу")
    void getAll_whenFiltered_shouldReturnCorrectPage() throws Exception {
        employeeService.create(validRequest); // Иван Петров
        employeeService.create(new EmployeeRequest("Владимир", "Иванов", "vlad@example.com", "111", "pass", Role.ADMIN));

        mockMvc.perform(get("/employees")
                        .param("name", "иван")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Иван"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }


    @Test
    @DisplayName("PUT /employees/{id}: должен обновить данные и вернуть 200 OK")
    void update_whenValidRequest_shouldUpdateDatabaseAndReturnOk() throws Exception {
        EmployeeResponse saved = employeeService.create(validRequest);

        EmployeeRequest updateRequest = new EmployeeRequest(
                "Иван", "Петровский", "new.email@example.com", "88888", "newPass", Role.ADMIN
        );

        mockMvc.perform(put("/employees/{id}", saved.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surname").value("Петровский"));

        assertThat(employeeRepository.findById(saved.id()).orElseThrow().getSurname()).isEqualTo("Петровский");
    }

    @Test
    @DisplayName("PUT /employees/{id}: должен вернуть 409 Conflict, если email занят другим")
    void update_whenNewEmailBelongsToAnother_shouldReturnConflict() throws Exception {
        employeeService.create(validRequest); // ivan.petrov@example.com
        EmployeeResponse emp2 = employeeService.create(new EmployeeRequest(
                "Владимир", "Иванов", "vlad@example.com", "111", "pass", Role.ADMIN
        ));

        EmployeeRequest conflictRequest = new EmployeeRequest(
                "Владимир", "Иванов", "ivan.petrov@example.com", "111", "pass123456", Role.ADMIN
        );

        mockMvc.perform(put("/employees/{id}", emp2.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("DELETE /employees/{id}: должен удалить сотрудника и вернуть 200 OK")
    void delete_whenExists_shouldRemoveFromDatabase() throws Exception {
        EmployeeResponse saved = employeeService.create(validRequest);

        mockMvc.perform(delete("/employees/{id}", saved.id()))
                .andExpect(status().isOk());

        assertThat(employeeRepository.findById(saved.id())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /employees/{id}: должен вернуть 404 Not Found, если сотрудника нет")
    void delete_whenNotExists_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/employees/{id}", 999999L))
                .andExpect(status().isNotFound());
    }
}
