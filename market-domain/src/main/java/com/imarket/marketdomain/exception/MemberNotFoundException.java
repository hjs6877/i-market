package com.imarket.marketdomain.exception;

public class MemberNotFoundException extends BusinessLogicException {
    public MemberNotFoundException() {
        super(ExceptionType.MEMBER_NOT_FOUND);
    }
}
