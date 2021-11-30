package com.imarket.marketdomain.exception;

public class ProductNotFoundException extends BusinessLogicException {
    public ProductNotFoundException() {
        super(ExceptionType.PRODUCT_NOT_FOUND);
    }
}
