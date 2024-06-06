package io.wanted.market.domain.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum CoreErrorType {
    DEFAULT_ERROR(CoreErrorCode.E5000, "예상치 못한 오류가 발생하였습니다.", LogLevel.ERROR),
    BAD_REQUEST_ERROR(CoreErrorCode.E4000, "잘못된 입력입니다.", LogLevel.INFO),
    FORBIDDEN_ERROR(CoreErrorCode.E4030, " 접근입니다.", LogLevel.INFO),
    NOT_FOUND_ERROR(CoreErrorCode.E4040, "해당 정보를 찾을 수 없습니다.", LogLevel.INFO),
    USER_NOT_FOUND_ERROR(CoreErrorCode.E4041, "해당 유저를 찾을 수 없습니다.", LogLevel.INFO);

    private final CoreErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    CoreErrorType(CoreErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }
}