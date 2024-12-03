package com.mykare.task.exception;

import com.mykare.task.enums.ExceptionType;

public class ErrorResponse {
    private ExceptionType exceptionType;
    private String message;
    public ErrorResponse(ExceptionType exceptionType, String message) {
        this.exceptionType = exceptionType;
        this.message = message;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
