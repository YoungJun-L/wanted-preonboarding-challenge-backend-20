package io.wanted.market.auth.api.support.error;

import lombok.Getter;

@Getter
public class AuthApiException extends RuntimeException {
    private final AuthErrorType authErrorType;

    private final Object data;

    public AuthApiException(AuthErrorType authErrorType) {
        super(authErrorType.getMessage());
        this.authErrorType = authErrorType;
        this.data = null;
    }

    public AuthApiException(AuthErrorType authErrorType, Object data) {
        super(authErrorType.getMessage());
        this.authErrorType = authErrorType;
        this.data = data;
    }
}
