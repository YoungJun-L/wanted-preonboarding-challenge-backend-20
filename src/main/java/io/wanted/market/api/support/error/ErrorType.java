package io.wanted.market.api.support.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "예상치 못한 오류가 발생하였습니다.", LogLevel.ERROR),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E400, "잘못된 입력입니다.", LogLevel.INFO),
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, ErrorCode.E403, "잘못된 접근입니다.", LogLevel.INFO),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, ErrorCode.E404, "해당 리소스를 찾을 수 없습니다.", LogLevel.INFO);

    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }
}
