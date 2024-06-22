package io.wanted.market.auth.api.security.response;

import io.wanted.market.auth.domain.token.TokenPair;

public record LoginResponseDto(
        Long userId,
        TokenResponseDto tokens
) {
    public static LoginResponseDto from(TokenPair tokenPair) {
        return new LoginResponseDto(tokenPair.authId(), TokenResponseDto.from(tokenPair));
    }
}
