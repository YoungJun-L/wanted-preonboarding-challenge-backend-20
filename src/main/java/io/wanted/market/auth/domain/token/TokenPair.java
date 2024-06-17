package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.storage.token.TokenEntity;

public record TokenPair(
        Long authId,
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
    public TokenEntity toEntity() {
        return new TokenEntity(authId, refreshToken);
    }
}
