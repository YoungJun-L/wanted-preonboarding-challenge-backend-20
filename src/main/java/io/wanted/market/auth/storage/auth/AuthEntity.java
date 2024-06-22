package io.wanted.market.auth.storage.auth;

import io.wanted.market.auth.domain.auth.AuthStatus;
import io.wanted.market.auth.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "auth")
@Entity
public class AuthEntity extends BaseEntity {
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private AuthStatus status;

    public AuthEntity(String username, String password, AuthStatus status) {
        this.username = username;
        this.password = password;
        this.status = status;
    }
}
