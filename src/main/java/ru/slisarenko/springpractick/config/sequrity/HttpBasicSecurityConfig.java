package ru.slisarenko.springpractick.config.sequrity;


import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import ru.slisarenko.springpractick.config.sequrity.configurer.HexConfigurer;
import ru.slisarenko.springpractick.config.sequrity.configurer.JwtAuthenticationConfigurer;
import ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ.AccessTokenJwsStringSerialize;
import ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ.RefreshTokenJwsStringSerialize;
import ru.slisarenko.springpractick.db.repositary.security.JdbcUserDetailRepositoryImpl;

import javax.sql.DataSource;
import java.text.ParseException;

@Slf4j
@Configuration
@EnableWebSecurity
public class HttpBasicSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailRepositoryImpl(dataSource);
    }

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey,
            @Value("${jwt.request-path}") String requestPath
    ) throws KeyLengthException, ParseException {
        var accessSigner = new MACSigner(OctetSequenceKey.parse(accessTokenKey));
        var refreshSigner = new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey));
        var accessTokenJwsStringSerialize = new AccessTokenJwsStringSerialize(accessSigner);
        var refreshTokenJwsStringSerialize = new RefreshTokenJwsStringSerialize(refreshSigner);

        var jwtAuthenticationConfigurer = new JwtAuthenticationConfigurer();
        jwtAuthenticationConfigurer.setJwtPath(requestPath);
        jwtAuthenticationConfigurer.setSecurityContextRepository(new RequestAttributeSecurityContextRepository());
        jwtAuthenticationConfigurer.setAccessTokenStringSerializer(accessTokenJwsStringSerialize);
        jwtAuthenticationConfigurer.setRefreshTokenStringSerializer(refreshTokenJwsStringSerialize);

        return jwtAuthenticationConfigurer;
    }

    @Bean
    public SecurityFilterChain basicSecurityFilterChain(HttpSecurity http,
                                                        JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {
        http.httpBasic(Customizer.withDefaults())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorizeRequest ->
                authorizeRequest
                        .anyRequest().authenticated()
                )
                .apply(new HexConfigurer());

        http.securityContext(httpSecuritySecurityContextConfigurer ->
                        httpSecuritySecurityContextConfigurer.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .apply(jwtAuthenticationConfigurer);

        return http.build();
    }
}
