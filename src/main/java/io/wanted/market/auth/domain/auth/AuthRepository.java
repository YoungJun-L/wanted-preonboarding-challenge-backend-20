package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthApiException;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.domain.user.User;
import io.wanted.market.auth.storage.auth.AuthEntity;
import io.wanted.market.auth.storage.auth.AuthJpaRepository;
import io.wanted.market.auth.storage.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AuthRepository {
    private final AuthJpaRepository authJpaRepository;

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public void save(User user, Auth auth) {
        Long authId = authJpaRepository.save(auth.toEntity()).getId();
        userJpaRepository.save(user.toEntityWith(authId));
    }

    public List<Auth> findByUsername(String username) {
        return authJpaRepository.findByUsername(username).stream()
                .map(entity -> Auth.enabled(entity.getUsername(), entity.getPassword()))
                .toList();
    }

    public boolean existsByUsername(String username) {
        return authJpaRepository.existsByUsername(username);
    }

    public Auth findById(Long id) {
        AuthEntity entity = authJpaRepository.findById(id)
                .orElseThrow(() -> new AuthApiException(AuthErrorType.AUTH_NOT_FOUND_ERROR));
        return Auth.from(entity);
    }
}
