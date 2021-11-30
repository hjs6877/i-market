package com.imarket.marketdomain.exception;

public class MemberDuplicatedException extends BusinessLogicException {
    public MemberDuplicatedException() {
        super(ExceptionType.MEMBER_DUPLICATED);
    }
}
