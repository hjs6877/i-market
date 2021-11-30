package com.imarket.marketapi.apis.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class SingleResponse<T> extends CommonResponse {
    private T data;

    public SingleResponse(HttpStatus status, T data) {
        super(status);
        this.data = data;
    }
}
