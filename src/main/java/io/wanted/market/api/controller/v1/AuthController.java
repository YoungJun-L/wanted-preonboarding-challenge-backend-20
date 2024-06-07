package io.wanted.market.api.controller.v1;

import io.wanted.market.api.controller.v1.request.LoginRequestDto;
import io.wanted.market.api.controller.v1.response.LoginResponseDto;
import io.wanted.market.api.support.response.ApiResponse;
import io.wanted.market.domain.token.TokenPair;
import io.wanted.market.domain.token.TokenService;
import io.wanted.market.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;

    private final TokenService tokenService;

    @PostMapping("/auth/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        Long userId = userService.validate(loginRequestDto.username(), loginRequestDto.password());
        TokenPair tokenPair = tokenService.issue(userId.toString());
        return ApiResponse.success(LoginResponseDto.from(tokenPair));
    }
}
