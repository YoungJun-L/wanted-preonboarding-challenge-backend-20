package io.wanted.market.auth.domain.auth;

import java.util.Optional;

public interface AuthRepository {
    Auth write(NewAuth newAuth);

    Optional<Auth> read(String username);

    boolean existsByUsername(String username);

    Optional<Auth> read(Long id);
}
