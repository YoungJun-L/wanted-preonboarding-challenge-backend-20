package io.wanted.market.api.support.response;

import io.wanted.market.api.support.error.ErrorMessage;
import io.wanted.market.api.support.error.ErrorType;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final StatusType status;

    private final T data;

    private final ErrorMessage error;

    private ApiResponse(StatusType status, T data, ErrorMessage error) {
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

    public static ApiResponse<Void> error(ErrorType errorType) {
        return new ApiResponse<>(StatusType.ERROR, null, new ErrorMessage(errorType));
    }

    public static ApiResponse<Void> error(ErrorType errorType, Object data) {
        return new ApiResponse<>(StatusType.ERROR, null, new ErrorMessage(errorType, data));
    }
}
