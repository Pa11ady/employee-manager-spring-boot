package ru.practicum.employeemanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.service.EmployeeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
@Tag(name = "Сотрудники", description = "Операции с сотрудниками")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Operation(summary = "Создать нового сотрудника")
    @PostMapping
    public EmployeeResponse create(@Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Добавление сотрудника {}", employeeRequest);
        return employeeService.create(employeeRequest);
    }

    @Operation(summary = "Получить сотрудника по ID", description = "Возвращает данные сотрудника, если он найден")
    @GetMapping("/{employeeId}")
    public EmployeeResponse get(
            @Parameter(description = "Уникальный идентификатор сотрудника", example = "123")
            @PathVariable long employeeId) {
        log.info("Получение сотрудника по id {}", employeeId);
        return employeeService.findById(employeeId);
    }

    @Operation(summary = "Получить всех сотрудников", description = "Возвращает список сотрудников с поддержкой фильтрации и пагинации")
    @GetMapping
    public Page<EmployeeResponse> getAll(
            @Parameter(description = "Фильтр по имени сотрудника")
            @RequestParam(required = false) String name,

            @Parameter(description = "Фильтр по фамилии сотрудника")
            @RequestParam(required = false) String surname,

            @Parameter(description = "Фильтр по email сотрудника")
            @RequestParam(required = false) String email,

            @Parameter(description = "Фильтр по телефону сотрудника")
            @RequestParam(required = false) String phone,

            @Parameter(description = "Параметры пагинации и сортировки (page, size, sort)")
            @PageableDefault(sort = "name") Pageable pageable) {
        log.info("Получение всех сотрудников");
        return employeeService.findAll(name, surname, email, phone, pageable);
    }

    @Operation(summary = "Обновление сотрудника")
    @PutMapping("/{employeeId}")
    public EmployeeResponse update(
            @Parameter(description = "Уникальный идентификатор сотрудника", example = "123")
            @PathVariable long employeeId,
            @Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Обновление сотрудника {} {}", employeeId, employeeRequest);
        return employeeService.update(employeeId, employeeRequest);
    }

    @Operation(summary = "Удаление сотрудника по ID")
    @DeleteMapping("/{employeeId}")
    public void delete(
            @Parameter(description = "Уникальный идентификатор сотрудника", example = "123")
            @PathVariable long employeeId) {
        log.info("Удаление сотрудника по id {}", employeeId);
        employeeService.delete(employeeId);
    }
}
