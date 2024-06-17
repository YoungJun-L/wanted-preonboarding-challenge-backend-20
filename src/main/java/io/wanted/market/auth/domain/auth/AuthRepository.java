package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import io.wanted.market.auth.storage.auth.AuthEntity;
import io.wanted.market.auth.storage.auth.AuthJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AuthRepository {
    private final AuthJpaRepository authJpaRepository;

    public Auth save(NewAuth newAuth) {
        return Auth.from(authJpaRepository.save(newAuth.toEntity()));
    }

    public List<Auth> findByUsername(String username) {
        return authJpaRepository.findByUsername(username).stream()
                .map(Auth::from)
                .toList();
    }

    public boolean existsByUsername(String username) {
        return authJpaRepository.existsByUsername(username);
    }

    public Auth findById(Long id) {
        AuthEntity entity = authJpaRepository.findById(id)
                .orElseThrow(() -> new AuthException(AuthErrorType.AUTH_NOT_FOUND_ERROR));
        return Auth.from(entity);
    }
}
