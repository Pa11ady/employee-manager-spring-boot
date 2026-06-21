package ru.practicum.employeemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.exception.EmailExistsException;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.mapper.EmployeeMapper;
import ru.practicum.employeemanager.model.Employee;
import ru.practicum.employeemanager.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.email())) {
            throw new EmailExistsException((employeeRequest.email() + " существует"));
        }
        Employee employee = employeeRepository.save(employeeMapper.toEntity(employeeRequest));
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findById(long id) {
        Employee employee = getEmployee(id);
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> findAll(String name, String surname, String email,  String phone, Pageable pageable) {
        Specification<Employee> spec = Specification.unrestricted();
        spec = addLike(spec, "name", name);
        spec = addLike(spec, "surname", surname);
        spec = addLike(spec, "email", email);
        spec = addLike(spec, "phone", phone);
        Page<Employee> page = employeeRepository.findAll(spec, pageable);
        return page.map(employeeMapper::toResponse);
    }

    private Specification<Employee> addLike(Specification<Employee> spec, String field, String value) {
        if (StringUtils.hasText(value)) {
            return spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%")
            );
        }
        return spec;
    }

    @Override
    @Transactional
    public EmployeeResponse update(long id, EmployeeRequest employeeRequest) {
        Employee employee = getEmployee(id);
        //если email свободен подставляем текущий id для успешной проверки
        Long existingId = employeeRepository.findIdByEmail(employeeRequest.email()).orElse(id);
        if (id != existingId) {
            throw new EmailExistsException((employeeRequest.email() + " существует"));
        }
        employeeMapper.updateEntityFromRequest(employeeRequest, employee);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    private Employee getEmployee(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сотрудник не найден: " + id));
    }

    @Override
    @Transactional
    public void delete(long id) {
        employeeRepository.delete(getEmployee(id));
    }
}
