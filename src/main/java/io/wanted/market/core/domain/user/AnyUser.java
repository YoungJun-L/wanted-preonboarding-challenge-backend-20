package io.wanted.market.core.domain.user;

public record AnyUser(Long id, UserInfo details) {
    public User toUser() {
        return new User(id, details);
    }
}
