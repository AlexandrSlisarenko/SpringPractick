package ru.slisarenko.springpractick.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

@Slf4j
@Configuration
public class SecurityControllers {

    @Bean
    public RouterFunction<ServerResponse> routes(HttpSecurity http) {
        return RouterFunctions.route()
                .GET("/api/v5/greetings", request -> {
                    var userDetailsService = request.principal().map(Authentication.class::cast)
                            .map(Authentication::getPrincipal)
                            .map(UserDetails.class::cast)
                            .orElseThrow();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Map.of("greeting", "Hello %s! V5"
                                    .formatted(userDetailsService.getUsername())));
                })
                .build();
    }
}
