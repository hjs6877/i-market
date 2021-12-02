package com.imarket.marketapi.apis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SToken {
    private String accessToken;
    private String refreshToken;

    public SToken() {}
}
