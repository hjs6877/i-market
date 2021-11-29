package com.imarket.marketapi.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(basePackages = {"com.imarket.marketdomain"})
@Configuration
public class JpaEnableConfiguration {
}
