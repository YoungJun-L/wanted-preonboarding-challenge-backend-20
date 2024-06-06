package io.wanted.market.api.controller.v1;

import io.wanted.market.api.controller.v1.request.RegisterRequestDto;
import io.wanted.market.api.support.response.ApiResponse;
import io.wanted.market.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/register")
    public ApiResponse<Void> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        userService.register(registerRequestDto.username(), registerRequestDto.password());
        return ApiResponse.success();
    }
}
