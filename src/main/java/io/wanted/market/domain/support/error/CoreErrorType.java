package io.wanted.market.domain.support.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum CoreErrorType {
    DEFAULT_ERROR(CoreErrorCode.E5000, "예상치 못한 오류가 발생하였습니다.", LogLevel.ERROR),
    BAD_REQUEST_ERROR(CoreErrorCode.E4000, "잘못된 입력입니다.", LogLevel.INFO),
    FORBIDDEN_ERROR(CoreErrorCode.E4030, "잘못된 접근입니다.", LogLevel.INFO),
    NOT_FOUND_ERROR(CoreErrorCode.E4040, "해당 정보를 찾을 수 없습니다.", LogLevel.INFO),

    // user
    USER_WRONG_PASSWORD_ERROR(CoreErrorCode.E4001, "잘못된 비밀번호입니다.", LogLevel.INFO),
    USER_DUPLICATE_ERROR(CoreErrorCode.E4002, "이미 존재하는 아이디입니다.", LogLevel.INFO),
    USER_BANNED_ERROR(CoreErrorCode.E4031, "잘못된 접근입니다.", LogLevel.WARN),
    USER_NOT_FOUND_ERROR(CoreErrorCode.E4041, "해당 유저를 찾을 수 없습니다.", LogLevel.INFO),

    // token
    TOKEN_INVALID_ERROR(CoreErrorCode.E4011, "토큰이 유효하지 않습니다.", LogLevel.INFO),
    TOKEN_EXPIRED_ERROR(CoreErrorCode.E4012, "토큰이 만료되었습니다.", LogLevel.INFO);

    private final CoreErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    CoreErrorType(CoreErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }
}
