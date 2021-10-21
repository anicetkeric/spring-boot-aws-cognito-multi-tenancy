package com.demo.aws.exceptions;

@SuppressWarnings("serial")
public class TenantDataNotFoundException extends ServiceException {

    public TenantDataNotFoundException() {
    }

    public TenantDataNotFoundException(String message) {
        super(message);
    }

    public TenantDataNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
