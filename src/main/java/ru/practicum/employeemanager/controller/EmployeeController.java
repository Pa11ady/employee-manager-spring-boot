package ru.practicum.employeemanager.controller;

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
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeResponse create(@Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Добавление сотрудника {}", employeeRequest);
        return employeeService.create(employeeRequest);
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponse get(@PathVariable long employeeId) {
        log.info("Получение сотрудника по id {}", employeeId);
        return employeeService.findById(employeeId);
    }

    @GetMapping()
    public Page<EmployeeResponse> getAll(
            @RequestParam(required = false) String name, @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email, @RequestParam(required = false) String phone,
            @PageableDefault(sort = "name") Pageable pageable) {
        log.info("Получение всех сотрудников");
        return employeeService.findAll(name, surname, email, phone, pageable);
    }

    @PutMapping("/{employeeId}")
    public EmployeeResponse update(@PathVariable long employeeId, @Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Обновление сотрудника {} {}", employeeId, employeeRequest);
        return employeeService.update(employeeId, employeeRequest);
    }

    @DeleteMapping("/{employeeId}")
    public void delete(@PathVariable long employeeId) {
        log.info("Удаление сотрудника по id {}", employeeId);
        employeeService.delete(employeeId);
    }
}
