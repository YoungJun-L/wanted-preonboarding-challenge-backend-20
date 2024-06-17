package io.wanted.market.auth.api.support.response;

import io.wanted.market.auth.api.support.error.AuthErrorMessage;
import io.wanted.market.auth.api.support.error.AuthErrorType;

public record AuthResponse<T>(
        AuthStatusType status,
        T data,
        AuthErrorMessage error
) {
    public static AuthResponse<Void> success() {
        return new AuthResponse<>(AuthStatusType.SUCCESS, null, null);
    }

    public static <T> AuthResponse<T> success(T data) {
        return new AuthResponse<>(AuthStatusType.SUCCESS, data, null);
    }

    public static AuthResponse<Void> error(AuthErrorType authErrorType) {
        return new AuthResponse<>(AuthStatusType.ERROR, null, new AuthErrorMessage(authErrorType));
    }

    public static AuthResponse<Void> error(AuthErrorType authErrorType, Object data) {
        return new AuthResponse<>(AuthStatusType.ERROR, null, new AuthErrorMessage(authErrorType, data));
    }
}
