package io.wanted.market.auth.storage.auth;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthRepository;
import io.wanted.market.auth.domain.auth.NewAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthCoreRepository implements AuthRepository {
    private final AuthJpaRepository authJpaRepository;

    public Auth write(NewAuth newAuth) {
        return Auth.from(authJpaRepository.save(newAuth.toEntity()));
    }

    public Optional<Auth> read(String username) {
        return authJpaRepository.findByUsername(username).map(Auth::from);
    }

    public boolean existsByUsername(String username) {
        return authJpaRepository.existsByUsername(username);
    }

    public Optional<Auth> read(Long id) {
        return authJpaRepository.findById(id).map(Auth::from);
    }
}
