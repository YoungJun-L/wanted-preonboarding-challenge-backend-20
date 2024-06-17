package io.wanted.market.auth.domain.token;

import io.wanted.market.core.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TokenEntity extends BaseEntity {
    private Long authId;

    private String refreshToken;

    public TokenEntity(Long authId, String refreshToken) {
        this.authId = authId;
        this.refreshToken = refreshToken;
    }
}
