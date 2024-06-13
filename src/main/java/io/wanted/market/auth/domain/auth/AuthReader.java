package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthApiException;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthReader {
    private final AuthRepository authRepository;

    public Auth read(String username) {
        List<Auth> auths = authRepository.findByUsername(username);
        if (auths.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }
        return auths.get(0);
    }

    public Auth read(Long id) {
        return authRepository.findById(id);
    }

    public void validateDuplicate(String username) {
        if (authRepository.existsByUsername(username)) {
            throw new AuthApiException(AuthErrorType.AUTH_DUPLICATE_ERROR);
        }
    }
}
