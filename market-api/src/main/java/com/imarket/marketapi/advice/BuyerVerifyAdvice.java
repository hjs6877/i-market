package com.imarket.marketapi.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = {"local", "dev"})
@Slf4j
@Component
@Aspect
public class BuyerVerifyAdvice extends VerifyAdvice {
    // TODO implementation
}
