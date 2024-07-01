package io.wanted.market.core.api.config;

import io.wanted.market.core.api.support.filter.AnyUserArgumentResolver;
import io.wanted.market.core.api.support.filter.UserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserArgumentResolver userArgumentResolver;

    private final AnyUserArgumentResolver anyUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
        resolvers.add(anyUserArgumentResolver);
    }
}
