package io.wanted.market.core.domain.user;

public record AnyUser(Long id, Object details) {
    public User toUser() {
        return new User(id, details);
    }

    public boolean isAuthenticated() {
        return id != -1;
    }
}
