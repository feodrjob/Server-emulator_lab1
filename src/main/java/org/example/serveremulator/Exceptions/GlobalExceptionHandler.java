package org.example.serveremulator.Exceptions;

import org.example.serveremulator.Enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        ErrorResponse error = new ErrorResponse(e);
        return ResponseEntity.status(e.getStatus()).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
        ErrorResponse error = new ErrorResponse(e);
        return ResponseEntity.status(e.getStatus()).body(error);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        ErrorResponse error = new ErrorResponse(e);
        return ResponseEntity.status(e.getStatus()).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse(
                ErrorCode.VALIDATION_ERROR.getCode(),
                e.getMessage(),
                "Invalid argument provided"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {
        System.err.println("Unhandled exception: " + e.getClass().getName());
        e.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                ErrorCode.INTERNAL_ERROR.getCode(),
                "Internal server error",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Создаем пустой список, куда будем складывать все тексты ошибок
        List<String> details = new ArrayList<>();

        // Проходимся по всем ошибкам, которые нашел Spring
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // Формируем строчку вида: "название_поля: текст_ошибки"
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }


        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.VALIDATION_ERROR.getCode(),
                "Ошибка валидации входных данных",
                details
        );

        // Возвращаем статус 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}