package io.wanted.market.auth.storage.token;

import io.wanted.market.auth.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    boolean existsByUserId(String userId);

    List<Token> findByUserId(String userId);

    void deleteByUserId(String userId);
}
