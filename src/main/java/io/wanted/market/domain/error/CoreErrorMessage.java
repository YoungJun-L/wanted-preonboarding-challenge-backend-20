package io.wanted.market.domain.error;

import lombok.Getter;

@Getter
public class CoreErrorMessage {
    private final CoreErrorCode code;

    private final String message;

    private final Object data;

    public CoreErrorMessage(CoreErrorType coreErrorType) {
        this.code = coreErrorType.getCode();
        this.message = coreErrorType.getMessage();
        this.data = null;
    }

    public CoreErrorMessage(CoreErrorType coreErrorType, Object data) {
        this.code = coreErrorType.getCode();
        this.message = coreErrorType.getMessage();
        this.data = data;
    }
}
