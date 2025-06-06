package ru.slisarenko.springpractick.config.sequrity.configurer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.slisarenko.springpractick.config.sequrity.filter.HexAuthenticationFilter;

public class HexConfigurer extends AbstractHttpConfigurer<HexConfigurer, HttpSecurity> {

    private AuthenticationEntryPoint authenticationEntryPoint =
            ((request, response, authException) -> {
                response.addHeader(HttpHeaders.AUTHORIZATION, "Hex");
                response.sendError(HttpStatus.UNAUTHORIZED.value());
            });
    //TODO: инициализация точки входа
    @Override
    public void init(HttpSecurity http) throws Exception {
        http.exceptionHandling(
                exception -> exception.authenticationEntryPoint(this.authenticationEntryPoint)
        );
    }

    //TODO: добавление фильтра в цепочку
    @Override
    public void configure(HttpSecurity http) throws Exception {
        var authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(new HexAuthenticationFilter(authenticationManager, this.authenticationEntryPoint),
                BasicAuthenticationFilter.class);
    }

    public HexConfigurer authenticationEntryPoint( AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        return this;
    }

}
