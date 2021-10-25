package com.demo.aws;

import com.amazonaws.services.cognitoidp.model.InternalErrorException;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.demo.aws.domain.enums.ErrorCodesEnum;
import com.demo.aws.domain.response.ErrorResponse;
import com.demo.aws.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UsernameExistsException.class, com.demo.aws.exceptions.UserNotFoundException.class})
    public ErrorResponse notFoundExceptionHandler(Exception ex) {
        ErrorCodesEnum errorCodes = ErrorCodesEnum.getErrorCode(HttpStatus.NOT_FOUND.value());
        return new ErrorResponse(String.valueOf(errorCodes.getCode()),errorCodes.getMessage(), ex.getMessage(), Collections.emptyList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ErrorResponse constraintViolationExceptionHandler(Exception ex) {
        ErrorCodesEnum errorCodes = ErrorCodesEnum.getErrorCode(HttpStatus.BAD_REQUEST.value());
        return new ErrorResponse(String.valueOf(errorCodes.getCode()),errorCodes.getMessage(), ex.getMessage(), Collections.emptyList());
    }


    @ExceptionHandler({FailedAuthenticationException.class, NotAuthorizedException.class, InvalidPasswordException.class , TenantDataNotFoundException.class, TenantAliasNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse unauthorizedExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex.getLocalizedMessage());
        ErrorCodesEnum errorCodes = ErrorCodesEnum.getErrorCode(HttpStatus.UNAUTHORIZED.value());
        return new ErrorResponse(String.valueOf(errorCodes.getCode()),errorCodes.getMessage(), ex.getMessage(), Collections.emptyList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptionHandler(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        ErrorCodesEnum errorCodes = ErrorCodesEnum.getErrorCode(HttpStatus.BAD_REQUEST.value());

        return new ErrorResponse(String.valueOf(errorCodes.getCode()),errorCodes.getMessage(), ex.getMessage(), errors);
    }

    @ExceptionHandler({Exception.class, InternalErrorException.class, ServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Exception ex) {
        log.error(ex.getMessage(), ex.getLocalizedMessage());
        ErrorCodesEnum errorCodes = ErrorCodesEnum.getErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ErrorResponse(String.valueOf(errorCodes.getCode()),errorCodes.getMessage(), ex.getMessage(), Collections.emptyList());
    }



}
