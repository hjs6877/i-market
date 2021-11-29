package com.imarket.marketapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MarketApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApiApplication.class, args);
    }
}
