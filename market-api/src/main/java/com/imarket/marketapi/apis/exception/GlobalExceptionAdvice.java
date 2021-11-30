package com.imarket.marketapi.apis.exception;

import com.imarket.marketapi.apis.response.ErrorResponse;
import com.imarket.marketdomain.exception.BusinessLogicException;
import com.imarket.marketdomain.exception.ExceptionType;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.nio.file.AccessDeniedException;

@Slf4j
@Profile(value = {"local", "dev", "prod"})
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("# handle MethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 메서드의 파라미터 타입이 일치 하지 않을 경우.
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("# handle MethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 비즈니스 로직에서 의도적으로 발생 시키는 예외를 처리
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessLogicException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessLogicException e) {
        log.error("# handle BusinessLogicException", e);
        final ExceptionType exceptionType = e.getExceptionType();
        final ErrorResponse response = ErrorResponse.of(exceptionType);
        return new ResponseEntity<>(response, HttpStatus.valueOf(exceptionType.getStatus()));
    }

    /**
     * 지원하지 않는 HTTP Method 호출 시, 예외 처리
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handle HttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Access Denied 예외 발생 시
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handle AccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * Binding하는 타입이 다를 경우 등 메시지를 읽을 수 없는경우
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("# handle HttpMessageNotReadableException", e);
        final ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * 파라미터를 입력하지 않았을 경우의 예외 처리
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("# handle MissingServletRequestParameterException", e);
        final ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * 토큰이 만료 되었을 경우의 예외 처리
     * @param e
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("# handle ExpiredJwtException", e);
        final ErrorResponse response = ErrorResponse.of(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * 서버에서 발생한 예외 처리
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("# handle Exception", e);
        // TODO implementation: send an email to Administrator

        final ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
