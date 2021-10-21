package com.demo.aws.domain.enums;

import lombok.Getter;

@Getter
public enum ErrorCodesEnum {

    INTERNAL_ERROR("S9000","Sorry, something went wrong. Please try again later."),
    SIGN_IN_ERROR("S7000","Sign-In Problem"),
    VALIDATION_ERROR("S5000","Validation failed"),
    NOT_FOUND_ERROR("S3000","Data not found!"),
    DUPLICATE_ERROR("S1000","Duplicate data");

    private final String code;
    private final String message;

    ErrorCodesEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
