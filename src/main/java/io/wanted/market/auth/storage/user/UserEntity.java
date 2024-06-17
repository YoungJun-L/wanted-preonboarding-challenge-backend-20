package io.wanted.market.auth.storage.user;

import io.wanted.market.auth.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "users")
public class UserEntity extends BaseEntity {
}
