package com.imarket.marketapi.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtInfo {
    @Value("${jwt.access_token_validity}")
    public long JWT_ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.refresh_token_validity}")
    public long JWT_REFRESH_TOKEN_VALIDITY;
    @Value("${jwt.secret}")
    public String secret;
}
