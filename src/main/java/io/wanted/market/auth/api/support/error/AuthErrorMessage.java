package io.wanted.market.auth.api.support.error;

import lombok.Getter;

@Getter
public class AuthErrorMessage {
    private final AuthErrorCode code;

    private final String message;

    private final Object data;

    public AuthErrorMessage(AuthErrorType authErrorType) {
        this.code = authErrorType.getCode();
        this.message = authErrorType.getMessage();
        this.data = null;
    }

    public AuthErrorMessage(AuthErrorType authErrorType, Object data) {
        this.code = authErrorType.getCode();
        this.message = authErrorType.getMessage();
        this.data = data;
    }
}
