package io.wanted.market.core.domain.support.error;

public record CoreErrorMessage(
        CoreErrorCode code,
        String message,
        Object data
) {
    public CoreErrorMessage(CoreErrorType coreErrorType) {
        this(coreErrorType.getCode(), coreErrorType.getMessage(), null);
    }

    public CoreErrorMessage(CoreErrorType coreErrorType, Object data) {
        this(coreErrorType.getCode(), coreErrorType.getMessage(), data);
    }
}
