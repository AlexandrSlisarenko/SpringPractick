package ru.slisarenko.springpractick.config.sequrity.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.slisarenko.springpractick.config.sequrity.filter.RequestJwtTokenFilter;
import ru.slisarenko.springpractick.config.sequrity.jwt.factory_token.DefaultAccessTokenFactory;
import ru.slisarenko.springpractick.config.sequrity.jwt.factory_token.DefaultRefreshTokenFactory;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;

import java.util.Objects;
import java.util.function.Function;

@Setter
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private String jwtPath = "/jwt/tokens";

    private RequestMatcher requestMatcher = new AntPathRequestMatcher(jwtPath, HttpMethod.POST.name());

    private SecurityContextRepository securityContextRepository;

    private Function<Authentication, Token> refreshTokenFactory = new DefaultRefreshTokenFactory();

    private Function<Token, Token> accessTokenFactory = new DefaultAccessTokenFactory();

    private Function<Token,String> refreshTokenStringSerializer;

    private Function<Token,String> accessTokenStringSerializer;

    private ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public void init(HttpSecurity builder) throws Exception {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (Objects.nonNull(csrfConfigurer)) {
            csrfConfigurer.ignoringRequestMatchers(requestMatcher);
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var requestJwtTokenFilter = new RequestJwtTokenFilter();
        requestJwtTokenFilter.setSecurityContextRepository(this.securityContextRepository);
        requestJwtTokenFilter.setRequestMatcher(this.requestMatcher);
        requestJwtTokenFilter.setRefreshTokenFactory(this.refreshTokenFactory);
        requestJwtTokenFilter.setAccessTokenFactory(this.accessTokenFactory);
        requestJwtTokenFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);
        requestJwtTokenFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);


        builder.addFilterAfter(requestJwtTokenFilter, ExceptionTranslationFilter.class);
    }

}
