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
import ru.practicum.employeemanager.dto.CustomerRequest;
import ru.practicum.employeemanager.dto.CustomerResponse;
import ru.practicum.employeemanager.dto.CustomerWithOrdersResponse;
import ru.practicum.employeemanager.service.CustomerService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Tag(name = "Клиенты", description = "Операции с клиентами")
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "Создать нового клиента")
    @PostMapping
    public CustomerResponse create(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Создание нового клиента");
        log.debug("Данные запроса на создание: {}", customerRequest);
        return customerService.create(customerRequest);
    }

    @Operation(summary = "Получить клиента по ID",
            description = "Возвращает данные клиента с его заказами, если он найден")
    @GetMapping("/{customerId}")
    public CustomerWithOrdersResponse get(
            @Parameter(description = "Уникальный идентификатор клиента", example = "123")
            @PathVariable long customerId) {
        log.info("Получение клиента по id {}", customerId);
        return customerService.findById(customerId);
    }

    @Operation(summary = "Получить всех клиентов", description = "Возвращает список клиентов с поддержкой пагинации")
    @GetMapping
    public Page<CustomerResponse> getAll(
            @Parameter(description = "Фильтр по имени клиента")
            @RequestParam(required = false) String name,

            @Parameter(description = "Фильтр по фамилии клиента")
            @RequestParam(required = false) String surname,

            @Parameter(description = "Фильтр по email клиента")
            @RequestParam(required = false) String email,

            @Parameter(description = "Фильтр по телефону клиента")
            @RequestParam(required = false) String phone,

            @Parameter(description = "Параметры пагинации и сортировки (page, size, sort)")
            @PageableDefault(sort = "name") Pageable pageable) {
        log.info("Запрос списка клиентов: name='{}', surname='{}', email='{}', phone='{}', pageable={}",
                name, surname, email, phone, pageable);
        return customerService.findAll(name, surname, email, phone, pageable);
    }

    @Operation(summary = "Обновление клиента")
    @PutMapping("/{customerId}")
    public CustomerResponse update(
            @Parameter(description = "Уникальный идентификатор клиента", example = "123")
            @PathVariable long customerId,
            @Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Обновление клиента с ID: {}", customerId);
        log.debug("Данные запроса на обновление: {}", customerRequest);
        return customerService.update(customerId, customerRequest);
    }

    @Operation(summary = "Удаление клиента по ID")
    @DeleteMapping("/{customerId}")
    public void delete(
            @Parameter(description = "Уникальный идентификатор клиента", example = "123")
            @PathVariable long customerId) {
        log.info("Удаление клиента по id {}", customerId);
        customerService.delete(customerId);
    }
}
