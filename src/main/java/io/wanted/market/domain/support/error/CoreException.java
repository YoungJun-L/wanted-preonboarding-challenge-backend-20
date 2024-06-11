package io.wanted.market.domain.support.error;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {
    private final CoreErrorType coreErrorType;

    private final Object data;

    public CoreException(CoreErrorType coreErrorType) {
        super(coreErrorType.getMessage());
        this.coreErrorType = coreErrorType;
        this.data = null;
    }

    public CoreException(CoreErrorType coreErrorType, Object data) {
        super(coreErrorType.getMessage());
        this.coreErrorType = coreErrorType;
        this.data = data;
    }
}
