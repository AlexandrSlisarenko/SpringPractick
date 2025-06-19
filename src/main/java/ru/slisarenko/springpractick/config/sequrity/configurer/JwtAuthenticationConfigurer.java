package ru.slisarenko.springpractick.config.sequrity.configurer;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.slisarenko.springpractick.config.sequrity.filter.JwtLogoutFilter;
import ru.slisarenko.springpractick.config.sequrity.filter.RefreshTokenFilter;
import ru.slisarenko.springpractick.config.sequrity.filter.RequestJwtTokenFilter;
import ru.slisarenko.springpractick.config.sequrity.jwt.converter.JwtAuthenticationConverter;
import ru.slisarenko.springpractick.config.sequrity.jwt.factory_token.DefaultAccessTokenFactory;
import ru.slisarenko.springpractick.config.sequrity.jwt.factory_token.DefaultRefreshTokenFactory;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;
import ru.slisarenko.springpractick.db.repositary.security.JdbcTokenLogoutRepository;
import ru.slisarenko.springpractick.service.TokenAuthenticationUserDetailsService;

import java.util.Objects;
import java.util.function.Function;

@Setter
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private static final String JWT_PATH = "/jwt/tokens";
    private static final String JWT_REFRESH_PATH = "/jwt/refresh";
    private static final String JWT_LOGOUT_PATH = "/jwt/logout";

    private String jwtPath = JWT_PATH;

    private String jwtRefreshPath = JWT_REFRESH_PATH;

    private String jwtLogoutPath = JWT_LOGOUT_PATH;

    private SecurityContextRepository securityContextRepository;

    private Function<Authentication, Token> refreshTokenFactory = new DefaultRefreshTokenFactory();

    private Function<Token, Token> accessTokenFactory = new DefaultAccessTokenFactory();

    private Function<Token, String> refreshTokenStringSerializer;

    private Function<Token, String> accessTokenStringSerializer;

    private Function<String, Token> refreshTokenStringDeserializer;

    private Function<String, Token> accessTokenStringDeserializer;

    private JdbcTokenLogoutRepository jwtTokenLogoutRepository;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        var requestMatcher = new AntPathRequestMatcher(jwtPath, RequestMethod.POST.name());

        if (Objects.nonNull(csrfConfigurer)) {
            csrfConfigurer.ignoringRequestMatchers(requestMatcher);
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var provider = createProvider();

        var requestJwtTokenFilter = createJwtTokenFilter();
        var jwtAuthenticationFilter = createJwtAuthenticationFilter(builder);
        var refreshTokenFilter = createJwtRefreshTokenFilter();
        var jwtLogoutFilter = createJwtLogoutFilter();


        builder.addFilterAfter(requestJwtTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(provider);


    }

    private RefreshTokenFilter createJwtRefreshTokenFilter() {
        var filter = new RefreshTokenFilter();

        filter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        filter.setAccessTokenFactory(this.accessTokenFactory);
        filter.setRequestMatcher(new AntPathRequestMatcher(jwtRefreshPath, HttpMethod.POST.name()));
        filter.setSecurityContextRepository(this.securityContextRepository);

        return filter;
    }

    private PreAuthenticatedAuthenticationProvider createProvider() {
        var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService(jwtTokenLogoutRepository));
        return provider;
    }

    private RequestJwtTokenFilter createJwtTokenFilter() {
        var filter = new RequestJwtTokenFilter();
        filter.setRequestMatcher(new AntPathRequestMatcher(jwtPath, HttpMethod.POST.name()));
        filter.setSecurityContextRepository(new RequestAttributeSecurityContextRepository());
        filter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);
        filter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        filter.setRefreshTokenFactory(this.refreshTokenFactory);
        filter.setAccessTokenFactory(this.accessTokenFactory);
        return filter;
    }

    private JwtLogoutFilter createJwtLogoutFilter() {
        var filter = new JwtLogoutFilter(jwtTokenLogoutRepository);
        filter.setRequestMatcher(new AntPathRequestMatcher(jwtLogoutPath, HttpMethod.POST.name()));
        filter.setSecurityContextRepository(new RequestAttributeSecurityContextRepository());

        return filter;
    }

    private AuthenticationFilter createJwtAuthenticationFilter(HttpSecurity builder) {

        var authenticationManager =
                builder.getSharedObject(AuthenticationManager.class);

        var authenticationConverter =
                new JwtAuthenticationConverter(this.accessTokenStringDeserializer, this.refreshTokenStringDeserializer);
        var jwtAuthenticationFilter =
                new AuthenticationFilter(authenticationManager, authenticationConverter);


        jwtAuthenticationFilter
                .setSuccessHandler((request, response, authentication) ->
                        CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter
                .setFailureHandler((request, response, exception) ->
                        response.sendError(HttpServletResponse.SC_FORBIDDEN));
        jwtAuthenticationFilter
                .setSecurityContextRepository(new RequestAttributeSecurityContextRepository());
        jwtAuthenticationFilter
                .setSecurityContextHolderStrategy(SecurityContextHolder.getContextHolderStrategy());



        return jwtAuthenticationFilter;
    }

}
