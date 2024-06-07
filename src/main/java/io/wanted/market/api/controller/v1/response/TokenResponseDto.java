package io.wanted.market.api.controller.v1.response;

import io.wanted.market.domain.token.TokenPair;

public record TokenResponseDto(
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
    public static TokenResponseDto from(TokenPair tokenPair) {
        return new TokenResponseDto(
                tokenPair.accessToken(),
                tokenPair.accessTokenExpiresIn(),
                tokenPair.refreshToken(),
                tokenPair.refreshTokenExpiresIn()
        );
    }
}
