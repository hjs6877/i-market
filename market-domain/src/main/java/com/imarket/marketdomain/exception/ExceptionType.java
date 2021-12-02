package com.imarket.marketdomain.exception;

import lombok.Getter;

public enum ExceptionType {
    PASSWORD_NOT_MATCH("Password not match", 409),
    MEMBER_DUPLICATED("Member duplicated", 409),
    MEMBER_NOT_FOUND("Member not found", 404),
    PRODUCT_NOT_FOUND("Product not found", 404),
    ORDER_NOT_FOUND("Order not found", 404),
    UNAUTHORIZED("Unauthorized", 401);

    @Getter
    private final String message;

    @Getter
    private final int status;

    ExceptionType(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
