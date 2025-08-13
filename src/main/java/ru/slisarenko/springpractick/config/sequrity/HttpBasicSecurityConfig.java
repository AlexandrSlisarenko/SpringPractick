package ru.slisarenko.springpractick.config.sequrity;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import ru.slisarenko.springpractick.config.sequrity.configurer.HexConfigurer;
import ru.slisarenko.springpractick.config.sequrity.configurer.JwtAuthenticationConfigurer;
import ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ.AccessTokenJwsStringSerialize;
import ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ.AccessTokenJwsStringDeserialize;
import ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ.RefreshTokenJwsStringDeserialize;
import ru.slisarenko.springpractick.config.sequrity.jwt.serializ_desirializ.RefreshTokenJwsStringSerialize;
import ru.slisarenko.springpractick.db.repositary.security.JdbcTokenLogoutRepository;
import ru.slisarenko.springpractick.db.repositary.security.JdbcTokenLogoutRepositoryImpl;
import ru.slisarenko.springpractick.db.repositary.security.JdbcUserDetailRepository;
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
    public JdbcTokenLogoutRepository tokenLogoutRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcTokenLogoutRepositoryImpl(jdbcTemplate);
    }


    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey,
            @Value("${jwt.request-path}") String requestPath,
            @Value("${jwt.refresh-path}") String refreshPath,
            @Value("${jwt.logout-path}") String logoutPath,
            JdbcTemplate jdbcTemplate
    ) throws JOSEException, ParseException {
        var accessSigner = new MACSigner(OctetSequenceKey.parse(accessTokenKey));
        var refreshSigner = new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey));

        var logoutRepository = new JdbcTokenLogoutRepositoryImpl(jdbcTemplate);

        var accessTokenJwsStringSerialize = new AccessTokenJwsStringSerialize(accessSigner);
        var refreshTokenJwsStringSerialize = new RefreshTokenJwsStringSerialize(refreshSigner);

        var accessTokenJwsStringDeserialize = new AccessTokenJwsStringDeserialize(new MACVerifier(OctetSequenceKey.parse(accessTokenKey)));
        var refreshTokenJwsStringDeserialize = new RefreshTokenJwsStringDeserialize(new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey)));

        var configurer = new JwtAuthenticationConfigurer();

        configurer.setJwtTokenLogoutRepository(logoutRepository);

        configurer.setJwtPath(requestPath);
        configurer.setJwtRefreshPath(refreshPath);
        configurer.setJwtLogoutPath(logoutPath);

        configurer.setSecurityContextRepository(new RequestAttributeSecurityContextRepository());

        configurer.setAccessTokenStringSerializer(accessTokenJwsStringSerialize);
        configurer.setRefreshTokenStringSerializer(refreshTokenJwsStringSerialize);

        configurer.setAccessTokenStringDeserializer(accessTokenJwsStringDeserialize);
        configurer.setRefreshTokenStringDeserializer(refreshTokenJwsStringDeserialize);

        return configurer;
    }

    @Bean
    public SecurityFilterChain basicSecurityFilterChain(HttpSecurity http,
                                                        JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {

        http.apply(jwtAuthenticationConfigurer);
        http.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(HttpMethod.POST, "/hello.html").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/public/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/error").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                ).sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }


}
