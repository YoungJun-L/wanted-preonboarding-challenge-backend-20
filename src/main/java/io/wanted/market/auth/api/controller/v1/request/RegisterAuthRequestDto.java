package io.wanted.market.auth.api.controller.v1.request;

import io.wanted.market.auth.domain.auth.AuthStatus;
import io.wanted.market.auth.domain.auth.NewAuth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterAuthRequestDto(
        @NotBlank
        @Pattern(regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d).{8,49}$", message = "Username validation error")
        String username,

        @NotBlank
        @Pattern(regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*+=-]).{10,49}$", message = "Password validation error")
        String password
) {
    public NewAuth toNewAuth() {
        return new NewAuth(username, password, AuthStatus.ENABLED);
    }
}
