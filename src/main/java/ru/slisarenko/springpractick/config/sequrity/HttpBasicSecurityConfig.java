package ru.slisarenko.springpractick.config.sequrity;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.slisarenko.springpractick.db.repositary.security.JdbcUserDetailRepositoryImpl;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

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

    public SecurityFilterChain basicSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(withDefaults())
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest.anyRequest().authenticated()
                )
                .build();
    }

    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception {
        http.apply(new CustomConfigurer());
        return http.build();
    }



}
