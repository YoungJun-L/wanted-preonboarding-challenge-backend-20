package io.wanted.market.support.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "예상치 못한 오류가 발생하였습니다.");

    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;

    ErrorType(HttpStatus status, ErrorCode code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
