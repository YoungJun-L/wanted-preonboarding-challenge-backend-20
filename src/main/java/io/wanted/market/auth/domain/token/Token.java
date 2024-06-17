package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.storage.token.TokenEntity;

public record Token(Long authId, String refreshToken) {
    public static Token from(TokenEntity tokenEntity) {
        return new Token(tokenEntity.getAuthId(), tokenEntity.getRefreshToken());
    }
}
