package io.wanted.market.core.domain.user;

public record User(Long id, UserInfo details) {
    public String username() {
        return details.username();
    }
}
