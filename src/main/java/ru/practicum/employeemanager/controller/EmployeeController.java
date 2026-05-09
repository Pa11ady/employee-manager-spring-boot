package ru.practicum.employeemanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    @PostMapping
    public EmployeeResponse create(@Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Добавление сотрудника {}", employeeRequest);
        return null;
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponse get(@PathVariable long employeeId) {
        log.info("Получение сотрудника по id {}", employeeId);
        return null;
    }

    @GetMapping()
    public List<EmployeeResponse> getAll() {
        log.info("Получение всех сотрудников");
        return new ArrayList<>();
    }

    @PutMapping("/{employeeId}")
    public EmployeeResponse update(@PathVariable long employeeId, @Valid @RequestBody EmployeeRequest employeeRequest) {
        log.info("Обновление сотрудника {} {}", employeeId, employeeRequest);
        return null;
    }

    @DeleteMapping("/{employeeId}")
    public void delete(@PathVariable long employeeId) {
        log.info("Удаление сотрудника по id {}", employeeId);
    }
}
