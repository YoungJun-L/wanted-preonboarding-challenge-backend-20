package io.wanted.market.domain.user;

import io.wanted.market.domain.support.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseEntity {
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = UserStatus.ENABLED;
    }

    public boolean isBanned() {
        return status.equals(UserStatus.BANNED);
    }
}
