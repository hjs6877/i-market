package com.imarket.marketapi.apis.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class CommonResponse {
    private String message;
    private int status;
    private List<ErrorResponse.GlobalError> globalErrors;
    private List<ErrorResponse.FieldError> fieldErrors;

    public CommonResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public CommonResponse(HttpStatus status) {
        this.status = status.value();
    }

    public CommonResponse(HttpStatus status, final List<ErrorResponse.FieldError> fieldErrors) {
        this.status = status.value();
        this.fieldErrors = fieldErrors;
    }

    public CommonResponse(HttpStatus status,
                          final List<ErrorResponse.GlobalError> globalErrors,
                          final List<ErrorResponse.FieldError> fieldErrors, boolean isSuccess) {
        this.status = status.value();
        this.globalErrors = globalErrors;
        this.fieldErrors = fieldErrors;
    }
}
