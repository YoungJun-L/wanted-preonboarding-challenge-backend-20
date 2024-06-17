package io.wanted.market.auth.storage.token;

import io.wanted.market.auth.domain.token.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, Long> {
    boolean existsByAuthId(Long authId);

    List<TokenEntity> findByAuthId(Long authId);

    void deleteByAuthId(Long authId);
}
