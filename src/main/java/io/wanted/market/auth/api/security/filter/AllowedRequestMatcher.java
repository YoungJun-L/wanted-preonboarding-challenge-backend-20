package io.wanted.market.auth.api.security.filter;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

@Getter
public enum AllowedRequestMatcher {
    REGISTER(HttpMethod.POST, "/auth/register"),
    REISSUE(HttpMethod.POST, "/auth/token"),
    HEALTH(HttpMethod.GET, "/health");

    private final HttpMethod method;

    private final String pattern;

    AllowedRequestMatcher(HttpMethod method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public static RequestMatcher[] matchers() {
        return Arrays.stream(AllowedRequestMatcher.values())
                .map(a -> AntPathRequestMatcher.antMatcher(a.method, a.pattern))
                .toArray(RequestMatcher[]::new);
    }
}
