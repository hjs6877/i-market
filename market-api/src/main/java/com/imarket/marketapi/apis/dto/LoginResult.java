package com.imarket.marketapi.apis.dto;

import lombok.Data;

@Data
public class LoginResult extends SToken {
    private Long memberId;
    private Long sellerId;
    private Long buyerId;

    public LoginResult() {}
    public LoginResult(Long memberId, Long buyerId, Long sellerId, String accessToken, String refreshToken) {
        super(accessToken, refreshToken);

        this.memberId = memberId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }
}
