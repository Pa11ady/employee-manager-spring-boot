package ru.practicum.employeemanager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.employeemanager.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Ошибка валидации запроса: {}", message);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Ошибка валидации: " + message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.warn("Ресурс не найден: {}", e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailExistsException(EmailExistsException e) {
        String message = "Ошибка: " + e.getMessage() + " Пожалуйста, используйте другой email.";
        log.warn("Попытка создания дубликата: {}", e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.value(), message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonParseError(HttpMessageNotReadableException e) {
        log.warn("Некорректный формат JSON в запросе: {}", e.getMostSpecificCause().getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Некорректный формат запроса. Проверьте типы данных.",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException e) {
        String message = e.getMostSpecificCause().getMessage();
        log.error("Нарушение целостности данных в БД: {}", message, e);
        if (message.contains("uc_product_name")) {
            return new ErrorResponse(
                    HttpStatus.CONFLICT.value(), "Название товара уже существует",
                    LocalDateTime.now()
            );
        } else if (message.contains("uc_order_product")) {
            return new ErrorResponse(
                    HttpStatus.CONFLICT.value(), "Товар уже существует в заказе",
                    LocalDateTime.now()
            );
        } else if (message.contains("fk_order_items_product_id")) {
            return new ErrorResponse(
                    HttpStatus.CONFLICT.value(), "Нельзя удалить товар, который используется в заказе",
                    LocalDateTime.now()
            );
        }
        throw e;
    }
}
