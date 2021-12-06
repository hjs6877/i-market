package com.imarket.marketapi.apis.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private String email;
    private String password;
}
