package ru.practicum.employeemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.exception.EmailExistsException;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.mapper.EmployeeMapper;
import ru.practicum.employeemanager.model.Employee;
import ru.practicum.employeemanager.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    //@Transactional не подключил в проект JPA
    public EmployeeResponse create(EmployeeRequest employeeRequest) {
        if (employeeRepository.findIdByEmail(employeeRequest.email()).isPresent() ) {
            throw new EmailExistsException((employeeRequest.email() + " существует"));
        }
        Employee employee = employeeRepository.create(employeeMapper.toEntity(employeeRequest));
        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse findById(long id) {
        Employee employee = getEmployee(id);
        return employeeMapper.toResponse(employee);
    }

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeMapper.toResponseList(employeeRepository.findAll());
    }

    @Override
    //@Transactional не подключил в проект JPA
    public EmployeeResponse update(long id, EmployeeRequest employeeRequest) {
        Employee employee = getEmployee(id);
        //если email свободен подставляем текущий id для успешной проверки
        Long existingId = employeeRepository.findIdByEmail(employeeRequest.email()).orElse(id);
        if (id != existingId) {
            throw new EmailExistsException((employeeRequest.email() + " существует"));
        }
        employeeMapper.updateEntityFromRequest(employeeRequest, employee);
        return employeeMapper.toResponse(employeeRepository.update(employee));
    }

    private Employee getEmployee(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сотрудник не найден: " + id));
    }

    @Override
    //@Transactional не подключил в проект JPA
    public void delete(long id) {
        Employee employee = getEmployee(id);
        employeeRepository.delete(employee.getId());
    }
}
