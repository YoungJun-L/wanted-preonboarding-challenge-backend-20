package io.wanted.market.auth.api.support.error;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final AuthErrorType authErrorType;

    private final Object data;

    public AuthException(AuthErrorType authErrorType) {
        super(authErrorType.getMessage());
        this.authErrorType = authErrorType;
        this.data = null;
    }

    public AuthException(AuthErrorType authErrorType, Object data) {
        super(authErrorType.getMessage());
        this.authErrorType = authErrorType;
        this.data = data;
    }
}
