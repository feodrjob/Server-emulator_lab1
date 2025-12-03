package org.example.serveremulator.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * ГЛОБАЛЬНЫЙ ОБРАБОТЧИК ИСКЛЮЧЕНИЙ
 *
 * ЗАЧЕМ НУЖЕН:
 * - Централизованная обработка ВСЕХ исключений в приложении
 * - Преобразует Java исключения в HTTP ответы
 * - Убирает дублирование try-catch в контроллерах
 * - Гарантирует единый формат всех ошибок
 * КАК РАБОТАЕТ:
 * - Аннотация @RestControllerAdvice делает его "глобальным"
 * - @ExceptionHandler ловит конкретные типы исключений
 * - Создает ErrorResponse и возвращает с правильным HTTP статусом
 */

@RestControllerAdvice  //ОБРАБАТЫВАЕТ ВСЕ КОНТРОЛЛЕРЫ В ПРИЛОЖЕНИИ
public class GlobalExceptionHandler {

    /**
     * ОБРАБОТКА: NotFoundException → HTTP 404 Not Found
     *
     * Когда срабатывает:
     * - Студент не найден по ID
     * - Группа не найдена по ID
     * - Любой другой объект не найден в БД
     *
     * Что делает:
     * 1. Ловит NotFoundException
     * 2. Создает ErrorResponse с сообщением из исключения
     * 3. Возвращает HTTP 404 с JSON ошибки
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        // Создаем структурированный ответ об ошибке
        ErrorResponse error = new ErrorResponse(
                e.getStatus(),      // Берём HttpStatus.NOT_FOUND из исключения
                e.getMessage(),     // "Student with id 5 not found"
                null                // Без дополнительных деталей
        );
        return ResponseEntity.status(e.getStatus()).body(error);
    }

    /**
     * ОБРАБОТКА: ValidationException → HTTP 400 Bad Request
     *
     * Когда срабатывает:
     * - Невалидные данные от клиента
     * - Нарушение бизнес-правил
     * - Конфликт расписания
     *
     * Что делает:
     * 1. Ловит ValidationException
     * 2. Создает ErrorResponse с сообщением об ошибке
     * 3. Возвращает HTTP 400 с JSON ошибки
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
        ErrorResponse error = new ErrorResponse(
                e.getStatus(),      // HttpStatus.BAD_REQUEST
                e.getMessage(),     // "First name cannot be empty"
                null                // Без деталей
        );
        return ResponseEntity.status(e.getStatus()).body(error);
    }

    /**
     * ОБРАБОТКА: IllegalArgumentException → HTTP 400 Bad Request
     *
     * Когда срабатывает:
     * - Неправильный формат ID (null или отрицательный)
     * - Другие стандартные проверки валидности
     *
     * Что делает:
     * 1. Ловит стандартные IllegalArgumentException
     * 2. Конвертирует в ValidationException
     * 3. Возвращает HTTP 400
     *
     * ЗАЧЕМ ОТДЕЛЬНО:
     * - Обратная совместимость со старым кодом
     * - Плавный переход на новые исключения
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST,  // Всегда 400 для невалидных аргументов
                e.getMessage(),          // Сообщение из исключения
                "Invalid argument provided"  // Контекст для разработчика
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * ОБРАБОТКА: Всех остальных исключений → HTTP 500 Internal Server Error
     *
     * Когда срабатывает:
     * - Любые непредвиденные ошибки
     * - Ошибки базы данных
     * - NullPointerException
     * - Любые RuntimeException не пойманные выше
     *
     * Что делает:
     * 1. Ловит Exception (все остальные исключения)
     * 2. Создает ErrorResponse с общим сообщением
     * 3. Возвращает HTTP 500
     * 4. Сохраняет детали для логирования (но не показываем клиенту)
     *
     * ВАЖНО: Не показываем stack trace клиенту (безопасность!)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {
        // Логируем полную ошибку для разработчиков (в консоль/лог-файл)
        System.err.println("Unhandled exception: " + e.getClass().getName());
        e.printStackTrace();

        // Клиенту показываем общее сообщение (без деталей)
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,  // HTTP 500
                "Internal server error",           // Общее сообщение для пользователя
                e.getMessage()                     // Детали ТОЛЬКО для разработки
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * ВСПОМОГАТЕЛЬНЫЙ МЕТОД для создания ответа из ApiException
     *
     * Можно использовать для всех кастомных исключений,
     * но лучше явно обрабатывать каждый тип для ясности
     */
    private ResponseEntity<ErrorResponse> createErrorResponse(ApiException e) {
        ErrorResponse error = new ErrorResponse(
                e.getStatus(),      // Берём статус из исключения
                e.getMessage(),     // Берём сообщение из исключения
                null                // Без дополнительных деталей
        );
        return ResponseEntity.status(e.getStatus()).body(error);
    }
}
