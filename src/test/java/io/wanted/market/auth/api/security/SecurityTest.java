package io.wanted.market.auth.api.security;

import io.wanted.market.auth.domain.auth.AuthService;
import io.wanted.market.auth.domain.token.TokenParser;
import io.wanted.market.auth.domain.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Tag("security")
@ExtendWith(RestDocumentationExtension.class)
@SpringJUnitWebConfig(SecurityTestConfig.class)
public abstract class SecurityTest {
    protected MockMvc mockMvc;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected TokenService tokenService;

    @Autowired
    protected TokenParser tokenParser;

    @BeforeEach
    void setUp(
            RestDocumentationContextProvider provider,
            WebApplicationContext context
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(provider))
                .apply(springSecurity())
                .build();
    }
}
