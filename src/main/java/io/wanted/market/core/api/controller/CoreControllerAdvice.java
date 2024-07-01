package io.wanted.market.core.api.controller;

import io.wanted.market.core.api.support.response.ApiResponse;
import io.wanted.market.core.domain.support.error.CoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CoreControllerAdvice {
    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(CoreException ex) {
        switch (ex.getCoreErrorType().getLogLevel()) {
            case ERROR -> log.error("CoreException : {}", ex.getMessage(), ex);
            case WARN -> log.warn("CoreException : {}", ex.getMessage(), ex);
            default -> log.info("CoreException : {}", ex.getMessage(), ex);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getCoreErrorType(), ex.getData()));
    }
}
