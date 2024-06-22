package io.wanted.market.core.api.support.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wanted.market.core.domain.support.error.CoreErrorType;
import io.wanted.market.core.domain.support.error.CoreException;
import io.wanted.market.core.domain.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String USER_COOKIE_NAME = "user";

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        try {
            HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
            Cookie cookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(USER_COOKIE_NAME))
                    .findFirst()
                    .orElseThrow();
            return objectMapper.readValue(cookie.getValue(), User.class);
        } catch (Exception ex) {
            throw new CoreException(CoreErrorType.FORBIDDEN_ERROR);
        }
    }
}
