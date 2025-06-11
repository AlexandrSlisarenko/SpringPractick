package ru.slisarenko.springpractick.config.sequrity.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.ParameterRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.slisarenko.springpractick.config.sequrity.filter.RefreshTokenFilter;
import ru.slisarenko.springpractick.config.sequrity.filter.RequestJwtTokenFilter;
import ru.slisarenko.springpractick.config.sequrity.jwt.converter.JwtAuthenticationConverter;
import ru.slisarenko.springpractick.config.sequrity.jwt.factory_token.DefaultAccessTokenFactory;
import ru.slisarenko.springpractick.config.sequrity.jwt.factory_token.DefaultRefreshTokenFactory;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;
import ru.slisarenko.springpractick.service.TokenAuthenticationUserDetailsService;

import java.util.Objects;
import java.util.function.Function;

@Setter
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private String jwtPath = "/jwt/tokens";


    private SecurityContextRepository securityContextRepository;

    private Function<Authentication, Token> refreshTokenFactory = new DefaultRefreshTokenFactory();

    private Function<Token, Token> accessTokenFactory = new DefaultAccessTokenFactory();

    private Function<Token, String> refreshTokenStringSerializer;

    private Function<Token, String> accessTokenStringSerializer;

    private Function<String, Token> refreshTokenStringDeserializer;

    private Function<String, Token> accessTokenStringDeserializer;


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
        var requestJwtTokenFilter = createJwtTokenFilter();
        var jwtAuthenticationFilter = createJwtAuthenticationFilter(builder);
        var provider = createprovider();

        var refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);


        builder.addFilterAfter(requestJwtTokenFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(provider);


    }

    private PreAuthenticatedAuthenticationProvider createprovider() {
        var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService());
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


        return jwtAuthenticationFilter;
    }

}
