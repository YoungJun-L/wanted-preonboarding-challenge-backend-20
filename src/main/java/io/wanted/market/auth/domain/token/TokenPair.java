package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.domain.auth.Auth;

public record TokenPair(
        Auth auth,
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
}
