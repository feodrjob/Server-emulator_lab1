package org.example.serveremulator.Exceptions;
// dto для ответов с ошибками
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final HttpStatus status;      // HTTP статус (404, 400, 500)
    private final String message;         // Сообщение об ошибке для пользователя
    private final String details;         // Детали для разработчика (опционально)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;  // Время возникновения ошибки

    public ErrorResponse(HttpStatus status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    // Конструктор для ошибок без деталей
    public ErrorResponse(HttpStatus status, String message) {
        this(status, message, null);
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
}