package io.wanted.market.auth.storage.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthJpaRepository extends JpaRepository<AuthEntity, Long> {
    List<AuthEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}