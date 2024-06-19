package io.wanted.market.auth.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.wanted.market.auth.api.security.filter.*;
import io.wanted.market.auth.api.security.support.StubPasswordEncoder;
import io.wanted.market.auth.domain.auth.AuthService;
import io.wanted.market.auth.domain.token.TokenParser;
import io.wanted.market.auth.domain.token.TokenService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.mockito.Mockito.mock;

@EnableWebSecurity
@TestConfiguration
class SecurityTestConfig {
    @Bean
    AuthService authService() {
        return mock(AuthService.class);
    }

    @Bean
    TokenService tokenService() {
        return mock(TokenService.class);
    }

    @Bean
    TokenParser tokenParser() {
        return mock(TokenParser.class);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AllowedRequestMatcher.matchers()).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAt(requestBodyUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(authDetailsExchangeFilter(), AuthorizationFilter.class)
                .authenticationManager(authenticationManager())
                .requestCache(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public RequestBodyUsernamePasswordAuthenticationFilter requestBodyUsernamePasswordAuthenticationFilter() {
        return new RequestBodyUsernamePasswordAuthenticationFilter(
                authenticationManager(),
                authenticationSuccessHandler(),
                authenticationFailureHandler(),
                objectMapper()
        );
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                bearerTokenResolver(),
                authenticationManager(),
                authenticationFailureHandler()
        );
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return new ProviderManager(
                jwtAuthenticationProvider(),
                daoAuthenticationProvider
        );
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new IssueJwtAuthenticationSuccessHandler(tokenService(), objectMapper());
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationEntryPointFailureHandler(authenticationEntryPoint());
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new ApiAuthenticationEntryPoint(objectMapper());
    }

    @Bean
    AuthDetailsExchangeFilter authDetailsExchangeFilter() {
        return new AuthDetailsExchangeFilter(objectMapper());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return authService();
    }

    @Bean
    BearerTokenResolver bearerTokenResolver() {
        return new BearerTokenResolver();
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(tokenParser(), authService());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new StubPasswordEncoder();
    }
}
