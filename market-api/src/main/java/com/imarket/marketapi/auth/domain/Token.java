package com.imarket.marketapi.auth.domain;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash
public class Token implements Serializable {

    private static final long serialVersionUID = 158092009196488699L;
    private String username;
    private String refreshToken;

}
