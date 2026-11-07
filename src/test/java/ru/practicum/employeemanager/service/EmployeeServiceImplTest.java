package ru.practicum.employeemanager.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.employeemanager.dto.EmployeeRequest;
import ru.practicum.employeemanager.dto.EmployeeResponse;
import ru.practicum.employeemanager.exception.EmailExistsException;
import ru.practicum.employeemanager.exception.NotFoundException;
import ru.practicum.employeemanager.model.Employee;
import ru.practicum.employeemanager.model.Role;
import ru.practicum.employeemanager.repository.EmployeeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@Transactional
@DisplayName("Интеграционные тесты EmployeeServiceImpl с реальным PostgreSQL (Zonky)")
class EmployeeServiceImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeRequest validRequest;
    private EmployeeRequest anotherValidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new EmployeeRequest(
                "Иван", "Петров", "ivan.petrov@example.com", "+79991234567", "securePass123", Role.MANAGER
        );
        anotherValidRequest = new EmployeeRequest(
                "Владимир", "Иванов", "vladimir@example.com", "12345", "securePass456", Role.ADMIN
        );
    }

    @Test
    @DisplayName("create: должен успешно создать сотрудника и реально сохранить все поля в БД")
    void create_whenValidRequest_shouldSaveAllFieldsToDatabase() {
        EmployeeResponse response = employeeService.create(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Иван");
        assertThat(response.email()).isEqualTo("ivan.petrov@example.com");

        //Принудительно отправляем отложенный INSERT в базу данных ПРЯМО СЕЙЧАС.
        entityManager.flush();
        //Полностью очищаем кэш первого уровня (Persistence Context)
        entityManager.clear();
        // Так как кэш пуст, Hibernate ОБЯЗАН выполнить реальный SQL-запрос SELECT к базе данных.
        Employee savedEntity = employeeRepository.findById(response.id())
                .orElseThrow(() -> new AssertionError("Сотрудник с ID " + response.id() + " не найден в БД"));

        assertThat(savedEntity.getName()).isEqualTo("Иван");
        assertThat(savedEntity.getSurname()).isEqualTo("Петров");
        assertThat(savedEntity.getEmail()).isEqualTo("ivan.petrov@example.com");
        assertThat(savedEntity.getPhone()).isEqualTo("+79991234567");
        assertThat(savedEntity.getRole()).isEqualTo(Role.MANAGER);
    }

    @Test
    @DisplayName("create: должен выбросить EmailExistsException, если email уже занят (без учета регистра)")
    void create_whenEmailAlreadyExists_shouldThrowException() {

        employeeService.create(validRequest);
        // Пытаемся сохранить второго с тем же email, но в другом регистре
        EmployeeRequest duplicateRequest = new EmployeeRequest(
                "Тест", "Тест", "IVAN.PETROV@example.com", "000", "pass", Role.MANAGER
        );

        assertThatThrownBy(() -> employeeService.create(duplicateRequest))
                .isInstanceOf(EmailExistsException.class)
                .hasMessageContaining("существует");
    }

    @Test
    @DisplayName("create: должен сохранить email в нижнем регистре")
    void create_shouldSaveEmailInLowerCase() {
        EmployeeRequest upperCaseEmailRequest = new EmployeeRequest(
                "Тест", "Тест", "TEST@EXAMPLE.COM", "111", "pass", Role.MANAGER
        );

        EmployeeResponse response = employeeService.create(upperCaseEmailRequest);

        assertThat(response.email()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("findById: должен вернуть сотрудника по существующему ID")
    void findById_whenExists_shouldReturnEmployee() {
        EmployeeResponse saved = employeeService.create(validRequest);

        EmployeeResponse found = employeeService.findById(saved.id());

        assertThat(found.id()).isEqualTo(saved.id());
        assertThat(found.name()).isEqualTo("Иван");
        assertThat(found.surname()).isEqualTo("Петров");
        assertThat(found.email()).isEqualTo("ivan.petrov@example.com");
    }

    @Test
    @DisplayName("findById: должен выбросить NotFoundException, если ID не существует")
    void findById_whenNotExists_shouldThrowException() {
        assertThatThrownBy(() -> employeeService.findById(999999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Сотрудник не найден: 999999");
    }

    @Test
    @DisplayName("findAll: должен корректно фильтровать по имени (LIKE, без учета регистра)")
    void findAll_whenFilterByName_shouldReturnFilteredPage() {
        employeeService.create(validRequest); // Иван
        employeeService.create(anotherValidRequest); // Владимир
        Pageable pageable = PageRequest.of(0, 10);

        Page<EmployeeResponse> result = employeeService.findAll("иван", null, null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Иван");
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("findAll: должен корректно фильтровать по фамилии")
    void findAll_whenFilterBySurname_shouldReturnFilteredPage() {
        employeeService.create(validRequest); // Петров
        employeeService.create(anotherValidRequest); // Иванов
        Pageable pageable = PageRequest.of(0, 10);

        Page<EmployeeResponse> result = employeeService.findAll(null, "петров", null,
                null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).surname()).isEqualTo("Петров");
    }

    @Test
    @DisplayName("findAll: должен корректно фильтровать по email")
    void findAll_whenFilterByEmail_shouldReturnFilteredPage() {
        employeeService.create(validRequest);
        employeeService.create(anotherValidRequest);
        Pageable pageable = PageRequest.of(0, 10);

        Page<EmployeeResponse> result = employeeService.findAll(null, null, "ivan", null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).email()).isEqualTo("ivan.petrov@example.com");
    }

    @Test
    @DisplayName("findAll: должен корректно фильтровать по телефону")
    void findAll_whenFilterByPhone_shouldReturnFilteredPage() {
        employeeService.create(validRequest); // +79991234567
        employeeService.create(anotherValidRequest);
        Pageable pageable = PageRequest.of(0, 10);

        Page<EmployeeResponse> result = employeeService.findAll(null, null, null, "9991234567", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).phone()).isEqualTo("+79991234567");
    }

    @Test
    @DisplayName("findAll: должен применять несколько фильтров одновременно")
    void findAll_whenMultipleFilters_shouldReturnFilteredPage() {
        employeeService.create(validRequest); // Иван Петров
        employeeService.create(anotherValidRequest); // Владимир Иванов
        Pageable pageable = PageRequest.of(0, 10);

        // ищем Ивана Петрова
        Page<EmployeeResponse> result = employeeService.findAll("Иван", "Петров", null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Иван");
        assertThat(result.getContent().get(0).surname()).isEqualTo("Петров");
    }

    @Test
    @DisplayName("findAll: должен вернуть пустую страницу, если фильтры не совпадают ни с кем")
    void findAll_whenNoMatch_shouldReturnEmptyPage() {
        employeeService.create(validRequest);
        Pageable pageable = PageRequest.of(0, 10);

        Page<EmployeeResponse> result = employeeService.findAll("НесуществующееИмя", null, null, null, pageable);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("findAll: должен поддерживать пагинацию")
    void findAll_whenPagination_shouldReturnCorrectPage() {
        for (int i = 1; i <= 5; i++) {
            EmployeeRequest request = new EmployeeRequest(
                    "Имя" + i, "Фамилия" + i, "email" + i + "@example.com", "111", "pass", Role.MANAGER
            );
            employeeService.create(request);
        }

        Pageable pageable = PageRequest.of(1, 2);
        Page<EmployeeResponse> result = employeeService.findAll(null, null, null, null, pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(1); // Номер текущей страницы
    }

    @Test
    @DisplayName("findAll: должен поддерживать сортировку")
    void findAll_whenSorting_shouldReturnSortedResults() {
        employeeService.create(new EmployeeRequest("Борис", "Тест", "boris@example.com",
                "1", "pass", Role.MANAGER));
        employeeService.create(new EmployeeRequest("Алексей", "Тест", "alexey@example.com",
                "2", "pass", Role.MANAGER));
        employeeService.create(new EmployeeRequest("Виктор", "Тест", "viktor@example.com",
                "3", "pass", Role.MANAGER));

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<EmployeeResponse> result = employeeService.findAll(null, null, null, null, pageable);

        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent().get(0).name()).isEqualTo("Алексей");
        assertThat(result.getContent().get(1).name()).isEqualTo("Борис");
        assertThat(result.getContent().get(2).name()).isEqualTo("Виктор");
    }

    @Test
    @DisplayName("findAll: должен вернуть всех сотрудников, если фильтры не указаны")
    void findAll_whenNoFilters_shouldReturnAllEmployees() {
        employeeService.create(validRequest);
        employeeService.create(anotherValidRequest);
        Pageable pageable = PageRequest.of(0, 10);

        Page<EmployeeResponse> result = employeeService.findAll(null, null, null, null, pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("update: должен успешно обновить данные сотрудника")
    void update_whenValidRequest_shouldUpdateEmployee() {
        EmployeeResponse saved = employeeService.create(validRequest);
        EmployeeRequest updateRequest = new EmployeeRequest(
                "Иван", "Петровский", "new.email@example.com", "88888", "newPass", Role.ADMIN
        );

        EmployeeResponse updated = employeeService.update(saved.id(), updateRequest);

        assertThat(updated.id()).isEqualTo(saved.id());
        assertThat(updated.name()).isEqualTo("Иван");
        assertThat(updated.surname()).isEqualTo("Петровский");
        assertThat(updated.email()).isEqualTo("new.email@example.com");
        assertThat(updated.phone()).isEqualTo("88888");
        assertThat(updated.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("update: должен позволить сохранить тот же email для того же сотрудника")
    void update_whenEmailUnchanged_shouldSucceed() {
        EmployeeResponse saved = employeeService.create(validRequest);
        // Меняем только фамилию, email оставляем абсолютно тем же
        EmployeeRequest updateRequest = new EmployeeRequest(
                "Иван", "НоваяФамилия", "ivan.petrov@example.com", "+79991234567", "securePass123", Role.MANAGER
        );

        EmployeeResponse updated = employeeService.update(saved.id(), updateRequest);

        assertThat(updated.surname()).isEqualTo("НоваяФамилия");
        assertThat(updated.email()).isEqualTo("ivan.petrov@example.com");
    }

    @Test
    @DisplayName("update: должен выбросить EmailExistsException, если email занят ДРУГИМ сотрудником")
    void update_whenEmailBelongsToAnotherEmployee_shouldThrowException() {
        employeeService.create(validRequest); // ivan.petrov@example.com
        EmployeeResponse second = employeeService.create(anotherValidRequest); // vladimir@example.com

        EmployeeRequest conflictRequest = new EmployeeRequest(
                "Владимир", "Иванов", "ivan.petrov@example.com", "12345", "pass", Role.ADMIN
        );

        assertThatThrownBy(() -> employeeService.update(second.id(), conflictRequest))
                .isInstanceOf(EmailExistsException.class)
                .hasMessageContaining("существует");
    }

    @Test
    @DisplayName("update: должен выбросить NotFoundException, если сотрудник не найден")
    void update_whenEmployeeNotFound_shouldThrowException() {
        EmployeeRequest updateRequest = new EmployeeRequest(
                "Тест", "Тест", "test@example.com", "111", "pass", Role.MANAGER
        );

        assertThatThrownBy(() -> employeeService.update(999999L, updateRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Сотрудник не найден: 999999");
    }

    @Test
    @DisplayName("delete: должен удалить сотрудника, и последующий поиск должен выбросить NotFoundException")
    void delete_whenExists_shouldRemoveEmployee() {
        EmployeeResponse saved = employeeService.create(validRequest);

        employeeService.delete(saved.id());

        assertThatThrownBy(() -> employeeService.findById(saved.id()))
                .isInstanceOf(NotFoundException.class);

        // Дополнительная проверка на уровне БД
        assertThat(employeeRepository.existsById(saved.id())).isFalse();
    }

    @Test
    @DisplayName("delete: должен выбросить NotFoundException при попытке удалить несуществующего сотрудника")
    void delete_whenNotExists_shouldThrowException() {
        assertThatThrownBy(() -> employeeService.delete(100500L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Сотрудник не найден: 100500");
    }
}
