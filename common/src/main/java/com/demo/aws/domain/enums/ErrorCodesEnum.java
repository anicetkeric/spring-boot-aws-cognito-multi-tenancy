package com.demo.aws.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ErrorCodesEnum {

    INTERNAL_ERROR(500,"Sorry, something went wrong. Please try again later."),
    SIGN_IN_ERROR(401,"Sign-In Problem"),
    VALIDATION_ERROR(400,"Validation failed"),
    NOT_FOUND_ERROR(404,"Data not found!"),
    DUPLICATE_ERROR(409,"Duplicate data"),
    UNAVAILABLE_ERROR(503,"Service Unavailable");

    private final int code;
    private final String message;


    /**
     * @param httpStatusCode http code
     * @return ErrorCodesEnum
     */
    public static ErrorCodesEnum getErrorCode(int httpStatusCode){

        return Arrays.stream(ErrorCodesEnum.values())
                .filter(e -> e.getCode() == httpStatusCode)
                .findFirst()
                .orElse(ErrorCodesEnum.UNAVAILABLE_ERROR);
    }
}
