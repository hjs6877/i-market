package com.imarket.marketapi.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@ComponentScan(basePackages = {"com.imarket.*"})
@EntityScan(basePackages = {"com.imarket.*"})
@EnableJpaRepositories(basePackages = {"com.imarket"})
@Configuration
public class JpaEnableConfiguration {
}
