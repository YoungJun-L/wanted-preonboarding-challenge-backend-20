package io.wanted.market.auth.api.controller.v1.response;

import io.wanted.market.auth.domain.token.TokenPair;

public record ReissueTokenResponseDto(
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
    public static ReissueTokenResponseDto from(TokenPair tokenPair) {
        return new ReissueTokenResponseDto(
                tokenPair.accessToken(),
                tokenPair.accessTokenExpiresIn(),
                tokenPair.refreshToken(),
                tokenPair.refreshTokenExpiresIn()
        );
    }
}
