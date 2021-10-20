package com.demo.aws.domain.enums;

import lombok.Getter;

@Getter
public enum ErrorCodesEnum {

    INTERNAL_ERROR("dd","421","ddd");

    private final String code;
    private final String message;
    /**
     * More details about the error here
     */
    private final String description;

    ErrorCodesEnum(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
