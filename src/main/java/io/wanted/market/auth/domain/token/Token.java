package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.storage.token.TokenEntity;

public record Token(Auth auth, String refreshToken) {
    public static Token from(Auth auth, TokenEntity tokenEntity) {
        return new Token(auth, tokenEntity.getRefreshToken());
    }
}
