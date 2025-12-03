package org.example.serveremulator.Exceptions;
//400 Bad Request - невалидные данные
import org.springframework.http.HttpStatus;
public class ValidationException extends ApiException{
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
