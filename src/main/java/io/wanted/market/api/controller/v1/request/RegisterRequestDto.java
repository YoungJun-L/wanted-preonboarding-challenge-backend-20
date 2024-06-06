package io.wanted.market.api.controller.v1.request;

public record RegisterRequestDto(
        String username,
        String password
) {
}
