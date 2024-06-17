package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthReader {
    private final AuthRepository authRepository;

    public Auth readEnabled(String username) {
        List<Auth> auths = authRepository.read(username);
        if (auths.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }
        Auth auth = auths.get(0);
        auth.validate();
        return auth;
    }

    public Auth readEnabled(Long id) {
        Auth auth = authRepository.read(id)
                .orElseThrow(() -> new AuthException(AuthErrorType.AUTH_NOT_FOUND_ERROR));
        auth.validate();
        return auth;
    }
}
