package org.example.serveremulator.Exceptions;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    private final HttpStatus status;  // HTTP статус (404, 400, 500)

    public ApiException(String message, HttpStatus status) {
        super(message);  // Сообщение об ошибке для логирования
        this.status = status;  // Сохраняем статус для ответа клиенту
    }

    public HttpStatus getStatus() {
        return status;  // Глобальный обработчик получит этот статус
    }
}