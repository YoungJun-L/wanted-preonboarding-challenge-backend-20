package io.wanted.market.core.api.support.response;

import io.wanted.market.core.domain.support.error.CoreErrorMessage;
import io.wanted.market.core.domain.support.error.CoreErrorType;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final ApiStatusType status;

    private final T data;

    private final CoreErrorMessage error;

    private ApiResponse(ApiStatusType status, T data, CoreErrorMessage error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ApiStatusType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiStatusType.SUCCESS, data, null);
    }

    public static ApiResponse<Void> error(CoreErrorType coreErrorType) {
        return new ApiResponse<>(ApiStatusType.ERROR, null, new CoreErrorMessage(coreErrorType));
    }

    public static ApiResponse<Void> error(CoreErrorType coreErrorType, Object data) {
        return new ApiResponse<>(ApiStatusType.ERROR, null, new CoreErrorMessage(coreErrorType, data));
    }
}
