package org.example.serveremulator.Exceptions;


import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private boolean success = false;
    private Integer errorCode;
    private String errorMessage;
    private List<String> details;

    //Конструктор по всем полям
    public ErrorResponse(Integer errorCode, String errorMessage, List<String> details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details != null ? details : new ArrayList<>();

    }
    //Конструктор для Apiexception
    public ErrorResponse(ApiException exception) {
        //Достаем код ошибки из Enum
        if (exception.getErrorCode() != null) {
            this.errorCode = exception.getErrorCode().getCode();
        }

        //Строки details складываем в массив (одна строка вроде)
        this.details = new ArrayList<>();
        if (exception.getDetails() != null) {
            this.details.add(exception.getDetails());
        }

    }

    //Конуструктор для непрведвиденных ошибок
    public ErrorResponse (Integer errorCode, String errorMessage, String detail){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = new ArrayList<>();
        if (detail != null) {
            this.details.add(detail);
        }
    }

    public boolean isSuccess() {return success;}
    public Integer getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}
    public List<String> getDetails() {return details;}
}