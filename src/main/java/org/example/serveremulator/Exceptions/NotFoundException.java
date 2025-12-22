package org.example.serveremulator.Exceptions;

import org.example.serveremulator.Enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(ErrorCode errorCode, String details) {
        super(errorCode, HttpStatus.NOT_FOUND, details);
    }

    // Конструктор для обратной совместимости
    public NotFoundException(String message) {
        super(ErrorCode.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, message);
    }
}