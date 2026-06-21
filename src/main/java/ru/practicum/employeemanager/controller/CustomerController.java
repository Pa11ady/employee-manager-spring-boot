package ru.practicum.employeemanager.controller;

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
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public CustomerResponse create(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Добавление клиента {}", customerRequest);
        return customerService.create(customerRequest);
    }

    @GetMapping("/{customerId}")
    public CustomerWithOrdersResponse get(@PathVariable long customerId) {
        log.info("Получение клиента по id {}", customerId);
        return customerService.findById(customerId);
    }

    @GetMapping()
    public Page<CustomerResponse> getAll(
            @RequestParam(required = false) String name, @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email, @RequestParam(required = false) String phone,
            @PageableDefault(sort = "name") Pageable pageable) {
        log.info("Получение всех клиентов");
        return customerService.findAll(name, surname, email, phone, pageable);
    }

    @PutMapping("/{customerId}")
    public CustomerResponse update(@PathVariable long customerId, @Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Обновление клиента {} {}", customerId, customerRequest);
        return customerService.update(customerId, customerRequest);
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable long customerId) {
        log.info("Удаление клиента по id {}", customerId);
        customerService.delete(customerId);
    }
}
