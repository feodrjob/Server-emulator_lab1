package org.example.serveremulator.Exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.serveremulator.Enums.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final String details;
    private final Integer errorCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;


    public ErrorResponse(HttpStatus status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.errorCode = null;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatus status, String message) {
        this(status, message, null);
    }



    public ErrorResponse(ApiException exception) {
        this.status = exception.getStatus();
        this.message = exception.getMessage();
        this.details = exception.getDetails();

        // Если у исключения есть ErrorCode - используем его
        if (exception.getErrorCode() != null) {
            this.errorCode = exception.getErrorCode().getCode();
        } else {
            this.errorCode = null;  // Для старых исключений
        }

        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(ErrorCode errorCode, HttpStatus status, String details) {
        this.status = status;
        this.message = errorCode.getMessage();
        this.details = details;
        this.errorCode = errorCode.getCode();
        this.timestamp = LocalDateTime.now();
    }


    public ErrorResponse(ErrorCode errorCode, HttpStatus status) {
        this(errorCode, status, null);
    }


    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public Integer getErrorCode() { return errorCode; }  // Может быть null!
    public LocalDateTime getTimestamp() { return timestamp; }
}