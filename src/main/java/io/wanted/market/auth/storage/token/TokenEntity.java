package io.wanted.market.auth.storage.token;

import io.wanted.market.auth.domain.token.Token;
import io.wanted.market.core.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "token")
@Entity
public class TokenEntity extends BaseEntity {
    private Long authId;

    private String refreshToken;

    public TokenEntity(Long authId, String refreshToken) {
        this.authId = authId;
        this.refreshToken = refreshToken;
    }

    public Token toToken() {
        return new Token(authId, refreshToken);
    }
}
