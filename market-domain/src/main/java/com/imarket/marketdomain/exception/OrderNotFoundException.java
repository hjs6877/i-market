package com.imarket.marketdomain.exception;

public class OrderNotFoundException extends BusinessLogicException {
    public OrderNotFoundException() {
        super(ExceptionType.ORDER_NOT_FOUND);
    }
}
