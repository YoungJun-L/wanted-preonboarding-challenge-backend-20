package io.wanted.market.auth.storage.token;

import io.wanted.market.core.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "token")
public class TokenEntity extends BaseEntity {
    private Long authId;

    private String refreshToken;

    public TokenEntity(Long authId, String refreshToken) {
        this.authId = authId;
        this.refreshToken = refreshToken;
    }
}
