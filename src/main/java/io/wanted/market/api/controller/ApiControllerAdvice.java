package io.wanted.market.api.controller;

import io.wanted.market.api.support.response.ApiResponse;
import io.wanted.market.domain.error.CoreErrorType;
import io.wanted.market.domain.error.CoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
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
public class ApiControllerAdvice {
    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(CoreException ex) {
        switch (ex.getCoreErrorType().getLogLevel()) {
            case ERROR -> log.error("ApiException : {}", ex.getMessage(), ex);
            case WARN -> log.warn("ApiException : {}", ex.getMessage(), ex);
            default -> log.info("ApiException : {}", ex.getMessage(), ex);
        }

        HttpStatus status = switch (ex.getCoreErrorType()) {
            case DEFAULT_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            case FORBIDDEN_ERROR -> HttpStatus.FORBIDDEN;
            case NOT_FOUND_ERROR -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(ex.getCoreErrorType(), ex.getData()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            BindException.class,
    })
    public ResponseEntity<ApiResponse<?>> handleBadRequest(final Exception ex) {
        log.debug("Bad Request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(CoreErrorType.BAD_REQUEST_ERROR));
    }

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(Exception ex) {
        log.debug("NotFound: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(CoreErrorType.NOT_FOUND_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(CoreErrorType.DEFAULT_ERROR));
    }
}
