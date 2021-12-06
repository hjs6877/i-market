package com.imarket.marketapi.apis.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SToken {
    private String accessToken;
    private String refreshToken;
}
