package io.wanted.market.auth.api.security.filter;

import org.springframework.security.core.AuthenticationException;

public class BadTokenException extends AuthenticationException {
    public BadTokenException(String msg) {
        super(msg);
    }

    public BadTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
