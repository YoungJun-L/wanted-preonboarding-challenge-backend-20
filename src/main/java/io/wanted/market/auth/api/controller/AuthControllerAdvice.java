package io.wanted.market.auth.api.controller;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import io.wanted.market.auth.api.support.response.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AuthResponse<?>> handleAuthException(AuthException ex) {
        switch (ex.getAuthErrorType().getLogLevel()) {
            case ERROR -> log.error("AuthException : {}", ex.getMessage(), ex);
            case WARN -> log.warn("AuthException : {}", ex.getMessage(), ex);
            default -> log.info("AuthException : {}", ex.getMessage(), ex);
        }
        return ResponseEntity
                .status(ex.getAuthErrorType().getStatus())
                .body(AuthResponse.error(ex.getAuthErrorType(), ex.getData()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
    })
    public ResponseEntity<AuthResponse<?>> handleBadRequest(Exception ex) {
        log.warn("Bad Request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(AuthResponse.error(AuthErrorType.BAD_REQUEST_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<AuthResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Bad Request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(AuthResponse.error(AuthErrorType.BAD_REQUEST_ERROR, ex.getFieldErrors().get(0).getField()));
    }

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<AuthResponse<?>> handleNotFoundException(Exception ex) {
        log.info("NotFound: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(AuthResponse.error(AuthErrorType.NOT_FOUND_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse<?>> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse.error(AuthErrorType.DEFAULT_ERROR));
    }
}
