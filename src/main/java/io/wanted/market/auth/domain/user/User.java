package io.wanted.market.auth.domain.user;

import io.wanted.market.auth.storage.user.UserEntity;

public record User() {
    public UserEntity toEntityWith(Long authId) {
        return new UserEntity(authId);
    }
}
