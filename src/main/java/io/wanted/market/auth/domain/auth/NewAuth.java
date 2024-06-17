package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.storage.auth.AuthEntity;

public record NewAuth(String username, String password, AuthStatus status) {
    public NewAuth encoded(String encodedPassword) {
        return new NewAuth(username, encodedPassword, status);
    }

    public AuthEntity toEntity() {
        return new AuthEntity(username, password, status);
    }
}
