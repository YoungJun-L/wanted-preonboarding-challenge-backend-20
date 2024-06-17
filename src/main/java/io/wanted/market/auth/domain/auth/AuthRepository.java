package io.wanted.market.auth.domain.auth;

import java.util.List;
import java.util.Optional;

public interface AuthRepository {
    Auth write(NewAuth newAuth);

    List<Auth> read(String username);

    boolean existsByUsername(String username);

    Optional<Auth> read(Long id);
}
