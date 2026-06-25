INSERT INTO employees (name, surname, email, phone, password, role)
VALUES ('Александр', 'Иванов', 'alexander.ivanov@company.ru', '+79001112233', 'admin123', 'ADMIN'),
       ('Мария', 'Петрова', 'maria.petrova@company.ru', '+79002223344', 'manager123', 'MANAGER'),
       ('Дмитрий', 'Сидоров', 'dmitry.sidorov@company.ru', '+79003334455', 'user123', 'MANAGER'),
       ('Елена', 'Козлова', 'elena.kozlova@company.ru', '+79004445566', 'user456', 'MANAGER');

INSERT INTO customers (name, surname, email, phone)
VALUES ('Иван', 'Смирнов', 'ivan.smirnov@mail.ru', '+79111234567'),
       ('Анна', 'Кузнецова', 'anna.kuznetsova@gmail.com', '+79122345678'),
       ('Пётр', 'Попов', 'petr.popov@yandex.ru', '+79133456789'),
       ('Светлана', 'Васильева', 'svetlana.vasileva@mail.ru', '+79144567890'),
       ('Михаил', 'Зайцев', 'mikhail.zaytsev@gmail.com', '+79155678901');

INSERT INTO orders (created_at, status, customer_id)
VALUES
-- Иван Смирнов
('2026-01-15 10:30:00+00', 'NEW', 1),
('2026-02-20 14:45:00+00', 'COMPLETED', 1),

-- Анна Кузнецова
('2026-03-10 09:15:00+00', 'PROCESSING', 2),

-- Пётр Попов
('2026-04-05 16:20:00+00', 'NEW', 3),
('2026-05-18 10:00:00+00', 'CANCELED', 3),

-- Светлана Васильева
('2026-06-01 14:15:00+00', 'PROCESSING', 4);

-- Михаил Зайцев 0 заказов