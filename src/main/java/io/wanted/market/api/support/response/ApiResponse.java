package io.wanted.market.api.support.response;

import io.wanted.market.domain.error.CoreErrorMessage;
import io.wanted.market.domain.error.CoreErrorType;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final StatusType status;

    private final T data;

    private final CoreErrorMessage error;

    private ApiResponse(StatusType status, T data, CoreErrorMessage error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(StatusType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(StatusType.SUCCESS, data, null);
    }

    public static ApiResponse<Void> error(CoreErrorType coreErrorType) {
        return new ApiResponse<>(StatusType.ERROR, null, new CoreErrorMessage(coreErrorType));
    }

    public static ApiResponse<Void> error(CoreErrorType coreErrorType, Object data) {
        return new ApiResponse<>(StatusType.ERROR, null, new CoreErrorMessage(coreErrorType, data));
    }
}
