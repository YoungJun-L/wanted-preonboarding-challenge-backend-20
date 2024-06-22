package io.wanted.market.auth.api.controller.v1.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueTokenRequestDto(
        @NotBlank
        String refreshToken
) {
}
