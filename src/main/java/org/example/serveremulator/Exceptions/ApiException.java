package org.example.serveremulator.Exceptions;
import org.example.serveremulator.Enums.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    private final HttpStatus status;    // HTTP статус (404, 400, 500)
    private final ErrorCode errorCode;  // Наш кастомный код (1000, 2000, etc)
    private final String details;       // Дополнительные детали

    public ApiException(ErrorCode errorCode, HttpStatus status) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.status = status;
        this.details = null;
    }

    public ApiException(ErrorCode errorCode, HttpStatus status, String details) {
        super(errorCode.getMessage() + (details != null ? ": " + details : ""));
        this.errorCode = errorCode;
        this.status = status;
        this.details = details;
    }

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.errorCode = ErrorCode.INTERNAL_ERROR;
        this.status = status;
        this.details = null;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}