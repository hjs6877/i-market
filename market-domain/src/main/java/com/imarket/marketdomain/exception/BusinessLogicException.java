package com.imarket.marketdomain.exception;

import lombok.Getter;

public class BusinessLogicException extends RuntimeException {
    @Getter
    private ExceptionType exceptionType;

    public BusinessLogicException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    public BusinessLogicException(String message, ExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
