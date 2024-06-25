package io.wanted.market.auth.api.security.filter;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

@Getter
public enum AllowedRequestMatcher {
    REGISTER(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/register")),
    REISSUE(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/token")),
    PRODUCT(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/products/**")),
    H2_CONSOLE(AntPathRequestMatcher.antMatcher("/h2-console/**")),
    API_DOCS(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/docs/**")),
    HEALTH(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/health"));

    private final RequestMatcher matcher;

    AllowedRequestMatcher(RequestMatcher matcher) {
        this.matcher = matcher;
    }

    public static RequestMatcher[] matchers() {
        return Arrays.stream(AllowedRequestMatcher.values())
                .map(a -> a.matcher)
                .toArray(RequestMatcher[]::new);
    }
}
