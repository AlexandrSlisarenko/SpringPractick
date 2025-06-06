package ru.slisarenko.springpractick.config.sequrity.jwt.factory_token;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.slisarenko.springpractick.config.sequrity.jwt.dto.Token;

import java.time.Duration;
import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Function;

import static java.time.Instant.now;

@Slf4j
public class DefaultRefreshTokenFactory implements Function<Authentication, Token> {

    @Setter
    private Duration tokenTtl = Duration.ofDays(1);

   @Override
    public Token apply(Authentication authentication) {
       var authorities = new LinkedList<String>();
       var instant = now();

       authorities.add("JWT_REFRESH");
       authorities.add("JWT_LOGOUT");
       authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> "GRAND_" + authority)
                .forEach(authorities::add);

       return new Token(UUID.randomUUID(),
                authentication.getName(),
                authorities,
                instant,
                instant.plus(tokenTtl));
    }
}
