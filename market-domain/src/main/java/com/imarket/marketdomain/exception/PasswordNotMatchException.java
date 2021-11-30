package com.imarket.marketdomain.exception;

public class PasswordNotMatchException extends BusinessLogicException {
    public PasswordNotMatchException() {
        super(ExceptionType.PASSWORD_NOT_MATCH);
    }
}
