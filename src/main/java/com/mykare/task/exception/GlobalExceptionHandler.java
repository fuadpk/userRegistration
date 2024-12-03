package com.mykare.task.exception;

import com.mykare.task.constants.ExceptionMessages;
import com.mykare.task.enums.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        return new ErrorResponse(ExceptionType.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlePasswordMismatch(Exception ex) {
        return new ErrorResponse(ExceptionType.AUTH_EXCEPTION, ExceptionMessages.INCORRECT_PASSWORD);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        return new ErrorResponse(ExceptionType.USER_EXCEPTION, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExists.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExists ex) {
        return new ErrorResponse(ExceptionType.USER_EXCEPTION, ex.getMessage());
    }
}
