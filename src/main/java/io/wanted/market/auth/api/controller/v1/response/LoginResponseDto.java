package io.wanted.market.auth.api.controller.v1.response;

import io.wanted.market.auth.domain.token.TokenPair;

public record LoginResponseDto(
        TokenResponseDto tokens
) {
    public static LoginResponseDto from(TokenPair tokenPair) {
        return new LoginResponseDto(TokenResponseDto.from(tokenPair));
    }
}
