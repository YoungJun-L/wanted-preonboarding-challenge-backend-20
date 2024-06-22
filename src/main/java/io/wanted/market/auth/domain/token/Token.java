package io.wanted.market.auth.domain.token;

public record Token(Long authId, String refreshToken) {
}
