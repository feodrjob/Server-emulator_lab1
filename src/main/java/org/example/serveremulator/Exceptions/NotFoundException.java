package org.example.serveremulator.Exceptions;
import org.springframework.http.HttpStatus;

//404 not found
public class NotFoundException extends ApiException {
    public NotFoundException(String entityName, Long id) {
        super(entityName + "with id" + id + "not found", HttpStatus.NOT_FOUND);
    }

    // Альтернативный конструктор для сообщений без ID
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
