package ru.practicum.employeemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.employeemanager.dto.CustomerRequest;
import ru.practicum.employeemanager.dto.CustomerResponse;
import ru.practicum.employeemanager.dto.CustomerWithOrdersResponse;
import ru.practicum.employeemanager.exception.EmailExistsException;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.mapper.CustomerMapper;
import ru.practicum.employeemanager.model.Customer;
import ru.practicum.employeemanager.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest customerRequest) {
        if (customerRepository.existsByEmail(customerRequest.email().toLowerCase())) {
            throw new EmailExistsException((customerRequest.email() + " существует"));
        }
        Customer customer = customerRepository.save(customerMapper.toEntity(customerRequest));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerWithOrdersResponse findById(long id) {
        Customer customer = customerRepository.findByIdWithOrders(id)
                .orElseThrow(() -> new NotFoundException("Клиент не найден: " + id));
        return customerMapper.toResponseFull(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> findAll(String name, String surname, String email, String phone, Pageable pageable) {
        Specification<Customer> spec = Specification.unrestricted();
        spec = addLike(spec, "name", name);
        spec = addLike(spec, "surname", surname);
        spec = addLike(spec, "email", email);
        spec = addLike(spec, "phone", phone);
        Page<Customer> page = customerRepository.findAll(spec, pageable);
        return page.map(customerMapper::toResponse);
    }

    private Specification<Customer> addLike(Specification<Customer> spec, String field, String value) {
        if (StringUtils.hasText(value)) {
            return spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%")
            );
        }
        return spec;
    }

    @Override
    @Transactional
    public CustomerResponse update(long id, CustomerRequest customerRequest) {
        Customer customer = getCustomer(id);
        //если email свободен подставляем текущий id для успешной проверки
        Long existingId = customerRepository.findIdByEmail(customerRequest.email().toLowerCase()).orElse(id);
        if (id != existingId) {
            throw new EmailExistsException((customerRequest.email() + " существует"));
        }
        customerMapper.updateEntityFromRequest(customerRequest, customer);
        //return customerMapper.toResponse(customerRepository.save(customer));
        // Объект customer находится в persistence context (managed),
        // поэтому вызов save() привёл бы к лишнему merge() без реального эффекта.
        // Hibernate сам выполнит UPDATE при flush в конце транзакции.
        //managed + @Transactional => save() не требуется
        return customerMapper.toResponse(customer);
    }

    private Customer getCustomer(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Клиент не найден: " + id));
    }

    @Override
    @Transactional
    public void delete(long id) {
        customerRepository.delete(getCustomer(id));
    }
}