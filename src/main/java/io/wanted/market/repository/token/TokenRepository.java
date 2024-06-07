package io.wanted.market.repository.token;

import io.wanted.market.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}
