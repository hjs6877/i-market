package com.imarket.marketapi.apis.response;

import com.imarket.marketdomain.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse<T> extends CommonResponse {
    private ErrorResponse(final String message, final int status) {
        super(message, status);
    }
    private ErrorResponse(final HttpStatus status) {
        super(status);
    }

    private ErrorResponse(final HttpStatus status, final List<FieldError> errors) {
        super(status, errors);
    }

    private ErrorResponse(final HttpStatus httpStatus, final List<GlobalError> globalErrors, final List<FieldError> fieldErrors) {
        super(httpStatus, globalErrors, fieldErrors, false);
    }

    public static ErrorResponse of(final ExceptionType exceptionType) {
        return new ErrorResponse(exceptionType.getMessage(), exceptionType.getStatus());
    }

    public static ErrorResponse of(final HttpStatus status) {
        return new ErrorResponse(status);
    }

    public static ErrorResponse of(final HttpStatus httpStatus, final BindingResult bindingResult) {
        return new ErrorResponse(httpStatus, GlobalError.of(bindingResult), FieldError.of(bindingResult));
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GlobalError {
        private String objectName;

        private String reason;

        private GlobalError(final String field, final String reason) {
            this.objectName = field;

            this.reason = reason;
        }

        public static List<GlobalError> of(final String field, final String reason) {
            List<GlobalError> globalErrors = new ArrayList<>();
            globalErrors.add(new GlobalError(field, reason));
            return globalErrors;
        }

        private static List<GlobalError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.ObjectError> objectErrors = bindingResult.getGlobalErrors();
            return objectErrors.stream()
                    .map(error -> new GlobalError(
                            error.getObjectName(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
