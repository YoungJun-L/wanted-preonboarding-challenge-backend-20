package io.wanted.market.auth.api.support.response;

import io.wanted.market.auth.api.support.error.AuthErrorMessage;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import lombok.Getter;

@Getter
public class AuthResponse<T> {
    private final AuthStatusType status;

    private final T data;

    private final AuthErrorMessage error;

    private AuthResponse(AuthStatusType status, T data, AuthErrorMessage error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

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
