package io.wanted.market.auth.api.controller.v1.request;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequestDto(
        @NotBlank
        @Pattern(regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d).{8,49}$", message = "Username validation error")
        String username,

        @NotBlank
        @Pattern(regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*+=-]).{10,49}$", message = "Password validation error")
        String password
) {
    public User toUser() {
        return new User();
    }

    public Auth toAuth() {
        return Auth.enabled(username, password);
    }
}
